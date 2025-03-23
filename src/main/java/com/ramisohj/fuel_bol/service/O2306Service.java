package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.OResultado2306;
import com.ramisohj.fuel_bol.repository.O2306Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class O2306Service {

    private final RestTemplate restTemplate;
    private final O2306Repository repository;

    @Value("${api.anh.estacion-saldo}")
    private String apiAnhEstacionSaldo;

    public O2306Service(O2306Repository repository) {
        this.restTemplate = new RestTemplate();
        this.repository = repository;
    }

    @Scheduled(fixedRate = 600000) // Runs every 10 minutes
    public void fetchDataFromApi() {
        String apiUrl = apiAnhEstacionSaldo + "/2306/0";
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            saveData(response.getBody());
        }
    }

    private void saveData(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("oResultado");
        List<OResultado2306> dataList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            OResultado2306 res_2306 = new OResultado2306();

            res_2306.setSaldo_octano(obj.getDouble("saldo_octano"));
            res_2306.setSaldo_bsa(obj.getDouble("saldo_bsa"));
            res_2306.setSaldo_planta(obj.getDouble("saldo_planta"));
            res_2306.setId_producto_bsa(obj.getInt("id_producto_bsa"));
            res_2306.setId_producto_hydro(obj.getInt("id_producto_hydro"));
            res_2306.setId_eess(obj.getInt("id_eess"));
            res_2306.setId_entidad(obj.getInt("id_entidad"));

            res_2306.setCreated_at(LocalDateTime.now());

            dataList.add(res_2306);
        }

        repository.saveAll(dataList);
        System.out.println("✅ Data with date saved successfully!");
    }
}
