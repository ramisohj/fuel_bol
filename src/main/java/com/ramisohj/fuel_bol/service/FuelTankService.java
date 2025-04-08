package com.ramisohj.fuel_bol.service;

import com.ramisohj.fuel_bol.model.FuelTank;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@Service
public class FuelTankService {

    private final DataSource dataSource;

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

                writer.flush(); // Still fine here, it's just flushing to the ByteArrayOutputStream

                try (InputStream is = new ByteArrayInputStream(baos.toByteArray())) {
                    copyManager.copyIn(copySql, is);
                }
            }
        } finally {
            // DO NOT close conn directly! Let Spring manage it.
            DataSourceUtils.releaseConnection(conn, dataSource); // Safe for transactional use
        }
    }
}
