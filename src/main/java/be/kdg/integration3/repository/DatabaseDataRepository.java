package be.kdg.integration3.repository;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.TemperatureData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("jdbcrepository")
public class DatabaseDataRepository implements DataRepository{
    private final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);
    private List<RawDataRecord> recordList;
    private JdbcTemplate jdbcTemplate;

    public DatabaseDataRepository(JdbcTemplate jdbcTemplate) {
        this.recordList = new ArrayList<>();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void read() {

    }

    @Override
    public void read(int id) {
        recordList = new ArrayList<>();

        List<TemperatureData> temperatures = jdbcTemplate.query("SELECT * FROM temperature_entry WHERE room_id = ?",
                (rs, rowNum) -> new TemperatureData(rs.getTimestamp("timestamp"), rs.getInt("value")), id);

        List<HumidityData> humidity = jdbcTemplate.query("SELECT * FROM humidity_entry WHERE room_id = ?",
                (rs, rowNum) -> new HumidityData(rs.getTimestamp("timestamp"), rs.getInt("value")), id);

        List<CO2Data> CO2 = jdbcTemplate.query("SELECT * FROM co2_entry WHERE room_id = ?",
                (rs, rowNum) -> new CO2Data(rs.getTimestamp("timestamp"), rs.getInt("value")), id);

        recordList.addAll(temperatures);
        recordList.addAll(humidity);
        recordList.addAll(CO2);
    }

    @Override
    public List<RawDataRecord> getRecordList() {
        return recordList;
    }
}
