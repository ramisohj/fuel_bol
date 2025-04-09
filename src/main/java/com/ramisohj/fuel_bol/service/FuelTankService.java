package com.ramisohj.fuel_bol.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ramisohj.fuel_bol.model.FuelCode;
import com.ramisohj.fuel_bol.model.FuelMonitoring;
import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.FuelTank;
import com.ramisohj.fuel_bol.model.FuelTankDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class FuelTankService {

    private final DataSource dataSource;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    public FuelTankService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Perform the bulk insert using the COPY command
    @Transactional
    public void bulkInsert(List<FuelTank> fuelTanks) throws SQLException, IOException {
        String copySql = "COPY fuel_tanks (id_monitoring, id_fuel_station, id_entity, id_product_bsa, id_product_hydro, fuel_type, level_bsa, level_octane, level_plant, created_at) FROM STDIN WITH (FORMAT csv)";

        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource); // transactional connection

            CopyManager copyManager = new CopyManager(conn.unwrap(BaseConnection.class));

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(baos)) {

                for (FuelTank tank : fuelTanks) {
                    writer.write(tank.toCsv());
                    writer.write("\n");
                }

                writer.flush();

                try (InputStream is = new ByteArrayInputStream(baos.toByteArray())) {
                    copyManager.copyIn(copySql, is);
                }
            }
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    // TODO: this method has better performance, use it only in a environment with good resources.
    public List<FuelTank> fetchFuelDataParallel(List<FuelStation> stations, FuelMonitoring monitoring, String apiFuelStation) {
        List<CompletableFuture<List<FuelTank>>> futures = stations.stream()
                .flatMap(station -> Arrays.stream(FuelCode.values())
                        .map(fuelCode -> CompletableFuture.supplyAsync(() -> {
                            try {
                                String url = apiFuelStation + "/" + station.getIdFuelStation() + "/" + fuelCode.ordinal();
                                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                                if (response.getStatusCode().is2xxSuccessful()) {
                                    return saveFuelTanks(response.getBody(), monitoring, fuelCode);
                                }
                            } catch (Exception e) {
                                System.out.printf("Failed API: station=%d code=%s - %s%n",
                                        station.getIdFuelStation(),
                                        fuelCode,
                                        e.getMessage());
                            }
                            return Collections.<FuelTank>emptyList();
                        }))
                )
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<FuelTank> fetchFuelDataSequential(List<FuelStation> stations, FuelMonitoring monitoring, String apiFuelStation) {
        List<FuelTank> allTanks = new ArrayList<>();

        for (FuelStation station : stations) {
            for (FuelCode fuelCode : FuelCode.values()) {
                try {
                    String url = apiFuelStation + "/" + station.getIdFuelStation() + "/" + fuelCode.ordinal();
                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        List<FuelTank> tanks = saveFuelTanks(response.getBody(), monitoring, fuelCode);
                        allTanks.addAll(tanks);
                    }
                } catch (Exception e) {
                    System.out.printf("Failed API: station=%d code=%s - %s%n",
                            station.getIdFuelStation(),
                            fuelCode,
                            e.getMessage()
                    );
                }
            }
        }
        return allTanks;
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(20);
    private final int batchSize = 20;
    private final long delayBetweenBatchesMs = 100;
    public List<FuelTank> fetchFuelDataBatched(List<FuelStation> stations, FuelMonitoring monitoring, String apiFuelStation) {

        List<FuelTank> allTanks = new ArrayList<>();
        AtomicInteger failFetchNumber = new AtomicInteger();

        for (int i = 0; i < stations.size(); i += batchSize) {
            List<FuelStation> batch = stations.subList(i, Math.min(i + batchSize, stations.size()));

            List<CompletableFuture<List<FuelTank>>> futures = batch.stream()
                    .flatMap(station -> Arrays.stream(FuelCode.values())
                            .map(fuelCode -> CompletableFuture.supplyAsync(() -> {
                                try {
                                    String url = apiFuelStation + "/" + station.getIdFuelStation() + "/" + fuelCode.ordinal();
                                    System.out.printf("Monitoring API: %s%n",
                                            url
                                    );
                                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                                    if (response.getStatusCode().is2xxSuccessful()) {
                                        return saveFuelTanks(response.getBody(), monitoring, fuelCode);
                                    }
                                } catch (Exception e) {
                                    System.out.printf("Failed API: station=%d code=%s - %s%n",
                                            station.getIdFuelStation(),
                                            fuelCode,
                                            e.getMessage()
                                    );
                                    failFetchNumber.getAndIncrement();
                                }
                                return Collections.<FuelTank>emptyList();
                            }, executor)) // << use custom executor
                    )
                    .toList();
            for (CompletableFuture<List<FuelTank>> future : futures) {
                try {
                    allTanks.addAll(future.get());
                } catch (Exception e) {
                    System.out.printf("Error joining future: %s%n", e.getMessage());
                }
            }
            try {
                Thread.sleep(delayBetweenBatchesMs); // Pause between batches
            } catch (InterruptedException ignored) {}
        }

        System.out.printf("Number of failed API: %d calls.%n", failFetchNumber.get());

        return allTanks;
    }

    private List<FuelTank> saveFuelTanks(String jsonResponse, FuelMonitoring fuelMonitoring, FuelCode fuelCode) {

        List<FuelTank> fuelTanks = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            if (jsonObject.has("oResultado") && !jsonObject.isNull("oResultado")) {
                JSONArray jsonArray = jsonObject.getJSONArray("oResultado");

                if (!jsonArray.isEmpty()) {

                    List<FuelTankDTO> dtoList = objectMapper.readValue(
                            jsonArray.toString(),
                            new TypeReference<List<FuelTankDTO>>() {}
                    );

                    LocalDateTime now = LocalDateTime.now();
                    List<FuelTank> fuelTankList = dtoList.stream().map(dto -> {
                        FuelTank fuelTank = new FuelTank();

                        fuelTank.setIdMonitoring(fuelMonitoring.getIdMonitoring());
                        fuelTank.setIdFuelStation(dto.getIdFuelStation());
                        fuelTank.setIdEntity(dto.getIdEntity());
                        fuelTank.setIdProductBsa(dto.getIdProductBsa());
                        fuelTank.setIdProductHydro(dto.getIdProductHydro());

                        fuelTank.setFuelType(fuelCode.toString());
                        fuelTank.setLevelBsa(dto.getLevelBsa());
                        fuelTank.setLevelOctane(dto.getLevelOctane());
                        fuelTank.setLevelPlant(dto.getLevelPlant());
                        fuelTank.setCreatedAt(now);

                        return fuelTank;
                    }).toList();

                    fuelTanks.addAll(fuelTankList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fuelTanks;
    }
}
