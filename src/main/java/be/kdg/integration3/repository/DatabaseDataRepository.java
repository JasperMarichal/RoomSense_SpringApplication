package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.util.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("jdbcrepository")
public class DatabaseDataRepository implements DataRepository{
    private final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);
    private List<TemperatureData> temperatureRecordList;
    private List<HumidityData> humidityRecordList;
    private List<CO2Data> CO2RecordList;
    private JdbcTemplate jdbcTemplate;

    public DatabaseDataRepository(JdbcTemplate jdbcTemplate) {
        this.temperatureRecordList = new ArrayList<>();
        this.humidityRecordList = new ArrayList<>();
        this.CO2RecordList = new ArrayList<>();
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Get the data from the database based on the roomId, resets the recordList at every read to avoid duplicate data
     * @param roomID The room ID to search for
     */
    @Override
    public void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        temperatureRecordList = new ArrayList<>();
        humidityRecordList = new ArrayList<>();
        CO2RecordList = new ArrayList<>();

        ZoneId userTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedEndDateTime = endDateTime.atZone(userTimeZone);
        ZonedDateTime zonedStartDateTime = startDateTime.atZone(userTimeZone);

        Timestamp timestampStart = Timestamp.from(zonedStartDateTime.toInstant());
        Timestamp timestampEnd = Timestamp.from(zonedEndDateTime.toInstant());

        try {
            getTemperatures(roomID, timestampEnd, timestampStart);
            getHumidity(roomID, timestampEnd, timestampStart);
            getCO2(roomID, timestampEnd, timestampStart);
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    private void getTemperatures(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<TemperatureData> temperatures = jdbcTemplate.query("SELECT * FROM temperature_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new TemperatureData(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        temperatureRecordList.addAll(temperatures);
    }

    private void getHumidity(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<HumidityData> humidity = jdbcTemplate.query("SELECT * FROM humidity_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new HumidityData(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        humidityRecordList.addAll(humidity);
    }

    private void getCO2(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<CO2Data> CO2 = jdbcTemplate.query("SELECT * FROM co2_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new CO2Data(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        CO2RecordList.addAll(CO2);
    }

    @Override
    public List<Room> getUserRooms(String userAccount) {
        try {
            return jdbcTemplate.query("SELECT * FROM room WHERE account = ?",
                    (rs, rowNum) -> new Room(rs.getInt("room_id"), rs.getString("room_name"),
                            rs.getDouble("length"), rs.getDouble("width"), rs.getDouble("height")), userAccount);
        } catch (DataAccessException e ){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    @Override
    public LocalDateTime getLastReadingTime(int roomID){
        try {
            List<TemperatureData> lastTemp = jdbcTemplate.query("SELECT * FROM temperature_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new TemperatureData(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            List<HumidityData> lastHumid = jdbcTemplate.query("SELECT * FROM humidity_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new HumidityData(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            List<CO2Data> lastCO2 = jdbcTemplate.query("SELECT * FROM co2_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new CO2Data(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            Timestamp lastTime = Timestamp.valueOf("1970-01-01 01:00:00");

            if (!lastTemp.isEmpty()) if (lastTime.compareTo(lastTemp.get(0).getTimestamp()) < 0) lastTime = lastTemp.get(0).getTimestamp();
            if (!lastHumid.isEmpty()) if (lastTime.compareTo(lastHumid.get(0).getTimestamp()) < 0) lastTime = lastHumid.get(0).getTimestamp();
            if (!lastCO2.isEmpty()) if (lastTime.compareTo(lastCO2.get(0).getTimestamp()) < 0) lastTime = lastCO2.get(0).getTimestamp();

            return  lastTime.toLocalDateTime();
        } catch (DataAccessException e ){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    @Override
    public List<TemperatureData> getTemperatureRecordList() {
        return temperatureRecordList;
    }

    @Override
    public List<HumidityData> getHumidityRecordList() {
        return humidityRecordList;
    }

    @Override
    public List<CO2Data> getCO2RecordList() {
        return CO2RecordList;
    }
}