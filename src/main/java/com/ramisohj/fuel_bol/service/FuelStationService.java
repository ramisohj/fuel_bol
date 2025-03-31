package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.DepartmentCode;
import com.ramisohj.fuel_bol.model.FuelCode;
import com.ramisohj.fuel_bol.model.FuelStation;
import com.ramisohj.fuel_bol.model.GeojsonPoint;
import com.ramisohj.fuel_bol.model.GeojsonPointList;
import com.ramisohj.fuel_bol.model.JsonPoint;
import com.ramisohj.fuel_bol.model.JsonPointList;
import com.ramisohj.fuel_bol.util.GeojsonLoader;
import com.ramisohj.fuel_bol.util.JsonLoader;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
import java.util.LinkedHashMap;


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
    public FuelStation getFuelStationById(long idFuelStation) {
        return fuelStationRepository.findById(idFuelStation).orElse(null);
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
    public List<FuelStation> getFuelStationsByIdDepartment(Long idDepartment) {
        return fuelStationRepository.findFuelStationsByIdDepartment(idDepartment);
    }

    @Transactional
    public List<FuelStation> getFuelStationsList() {
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

    @Transactional
    public GeojsonPoint getGeojsonPointFuelStation(long idFuelStation) {
        FuelStation fuelStation = getFuelStationById(idFuelStation);
        Map<String, Object> properties = getFuelStationGeojsonProperties(fuelStation);
        return GeojsonLoader.generateGeojsonPoint(properties);
    }

    @Transactional
    public GeojsonPointList getGeojsonPointFuelStationList() {
        List<FuelStation> fuelStationList = getFuelStationsList();
        List<Map<String, Object>> propertiesList = new ArrayList<>();
        for (FuelStation fuelStation : fuelStationList) {
            Map<String, Object> properties = getFuelStationGeojsonProperties(fuelStation);
            propertiesList.add(properties);
        }
        return GeojsonLoader.generateGeojsonPointList(propertiesList);
    }

    private Map<String, Object> getFuelStationGeojsonProperties(FuelStation fuelStation) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("idFuelStation", fuelStation.getIdFuelStation());
        properties.put("idEntity", fuelStation.getIdEntity());
        properties.put("idDepartment", fuelStation.getIdDepartment());
        properties.put("fuelStationName", fuelStation.getFuelStationName());
        properties.put("direction", fuelStation.getDirection());
        properties.put("createdAt", fuelStation.getCreatedAt());
        return properties;
    }

    @Transactional
    public JsonPoint getJsonPointFuelStation(long idFuelStation) {
        FuelStation fuelStation = getFuelStationById(idFuelStation);
        Map<String, Object> properties = getFuelStationJsonProperties(fuelStation);
        return JsonLoader.generateJsonPoint(properties);
    }

    @Transactional
    public JsonPointList getJsonPointFuelStationList() {
        List<FuelStation> fuelStationList = getFuelStationsList();
        List<Map<String, Object>> propertiesList = new ArrayList<>();
        for (FuelStation fuelStation : fuelStationList) {
            Map<String, Object> properties = getFuelStationJsonProperties(fuelStation);
            propertiesList.add(properties);
        }
        return JsonLoader.generateJsonPointList(propertiesList);
    }

    private Map<String, Object> getFuelStationJsonProperties(FuelStation fuelStation) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("idFuelStation", fuelStation.getIdFuelStation());
        properties.put("idEntity", fuelStation.getIdEntity());
        properties.put("idDepartment", fuelStation.getIdDepartment());
        properties.put("fuelStationName", fuelStation.getFuelStationName());
        properties.put("direction", fuelStation.getDirection());
        properties.put("longitude", fuelStation.getLocation().getX());
        properties.put("latitude ", fuelStation.getLocation().getY());
        properties.put("createdAt", fuelStation.getCreatedAt());
        return properties;
    }

}
