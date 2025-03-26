package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.DepartmentCode;
import com.ramisohj.fuel_bol.model.FuelCode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.repository.FuelStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class FuelStationService {

    private final FuelStationRepository fuelStationRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    @Value("${api.anh.fuel-station.list}")
    private String apiFuelStationList;

    @Autowired
    public FuelStationService(FuelStationRepository fuelStationRepository) {
        this.fuelStationRepository = fuelStationRepository;
    }

    @Transactional
    public List<FuelStation> saveUniqueFuelStation(List<FuelStation> stations) {

        List<FuelStation> savedFuelStations = new ArrayList<>();

        for (FuelStation station : stations) {
            if (!fuelStationRepository.existsById(station.getIdFuelStation())) {
                Point point = geometryFactory.createPoint(new Coordinate(
                        station.getLocation().getX(), station.getLocation().getY()
                ));
                station.setLocation(point);
                fuelStationRepository.save(station);
                savedFuelStations.add(station);
            }
        }
        return savedFuelStations;
    }

    @Transactional
    public List<FuelStation> getFuelStationsByDept(Long id_dept) {
        return fuelStationRepository.findFuelStationsByIdDepartment(id_dept);
    }

    @Transactional
    public List<FuelStation> getFuelStationsFromDB() {
        return fuelStationRepository.findAll();
    }

    public List<FuelStation> getFuelStationsFromAPI() {

        List<FuelStation> FuelStations = new ArrayList<>();

        for(int deptCode = DepartmentCode.CHUQUISACA.ordinal(); deptCode<= DepartmentCode.PANDO.ordinal(); deptCode++) {
            for(int fuelCode = FuelCode.GASOLINE.ordinal(); fuelCode<=FuelCode.ULS_DIESEL.ordinal(); fuelCode++) {
                RestTemplate restTemplate = new RestTemplate();
                Map response = restTemplate.getForObject(apiFuelStationList + "/"+deptCode+"/"+fuelCode, Map.class);

                if (response == null || !response.containsKey("oResultado")) {
                    String errorMsg = "Error: oResultado is missing from the response";
                    System.out.println(errorMsg);
                    continue;
                }

                Object resultadoObj = response.get("oResultado");
                if (!(resultadoObj instanceof List)) {
                    String errorMsg = "Error: oResultado is not a list";
                    System.out.println(errorMsg);
                    continue;
                }

                List<Map<String, Object>> oResultado = (List<Map<String, Object>>) resultadoObj;

                if (oResultado.isEmpty()) {
                    String warningMsg = "Warning: oResultado is empty";
                    System.out.println(warningMsg);
                    continue;
                }

                List<FuelStation> stations = oResultado.stream()
                        .map(obj -> {
                            try {
                                Point location = geometryFactory.createPoint(new Coordinate(
                                        Double.parseDouble(obj.get("longitud").toString()),
                                        Double.parseDouble(obj.get("latitud").toString())
                                ));

                                return new FuelStation(
                                        Long.valueOf(obj.get("id_eess_saldo").toString()),
                                        Long.valueOf(obj.get("id_entidad").toString()),
                                        Long.valueOf(obj.get("id_departamento").toString()),
                                        obj.get("nombreEstacion").toString(),
                                        obj.get("direccion").toString(),
                                        location,
                                        LocalDateTime.now()
                                );
                            } catch (Exception e) {
                                System.out.println("Error processing object: " + obj + ", date: " + LocalDateTime.now());
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();
                FuelStations.addAll(stations);
            }
        }
        return FuelStations;
    }
}
