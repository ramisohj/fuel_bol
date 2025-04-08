package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.FuelMonitoring;
import com.ramisohj.fuel_bol.repository.FuelMonitoringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;


@Service
public class FuelMonitoringService {

    private final FuelMonitoringRepository fuelMonitoringRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public FuelMonitoringService(FuelMonitoringRepository fuelMonitoringRepository) {
        this.fuelMonitoringRepository = fuelMonitoringRepository;
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional
    public FuelMonitoring insertFuelMonitoring(FuelMonitoring monitoring) {
        String sql = "INSERT INTO fuel_monitoring (monitoring_at, created_at) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, Timestamp.valueOf(monitoring.getMonitoringAt()));
            ps.setTimestamp(2, Timestamp.valueOf(monitoring.getCreatedAt()));
            return ps;
        }, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id_monitoring")) {
            monitoring.setIdMonitoring(((Number) keys.get("id_monitoring")).longValue());
        }

        return monitoring;
    }

}
