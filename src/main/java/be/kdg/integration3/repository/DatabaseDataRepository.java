package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.util.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbcrepository")
public class DatabaseDataRepository implements DataRepository {
    private final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);
    private List<TemperatureData> temperatureRecordList;
    private List<HumidityData> humidityRecordList;
    private List<CO2Data> CO2RecordList;
    private List<SoundData> noiseRecordList;
    private List<SoundSpike> spikeRecordList;
    private JdbcTemplate jdbcTemplate;

    public DatabaseDataRepository(JdbcTemplate jdbcTemplate) {
        this.temperatureRecordList = new ArrayList<>();
        this.humidityRecordList = new ArrayList<>();
        this.CO2RecordList = new ArrayList<>();
        this.noiseRecordList = new ArrayList<>();
        this.spikeRecordList = new ArrayList<>();
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Runs the methods to get all the data between 2 timestamps from a certain room from the database
     * @param roomID the room to look in
     * @param startDateTime the start time
     * @param endDateTime the end time
     */
    @Override
    public void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean readSpikes) {
        temperatureRecordList = new ArrayList<>();
        humidityRecordList = new ArrayList<>();
        CO2RecordList = new ArrayList<>();
        noiseRecordList = new ArrayList<>();
        spikeRecordList = new ArrayList<>();

        ZoneId userTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedEndDateTime = endDateTime.atZone(userTimeZone);
        ZonedDateTime zonedStartDateTime = startDateTime.atZone(userTimeZone);

        Timestamp timestampStart = Timestamp.from(zonedStartDateTime.toInstant());
        Timestamp timestampEnd = Timestamp.from(zonedEndDateTime.toInstant());

        try {
            getTemperatures(roomID, timestampEnd, timestampStart);
            getHumidity(roomID, timestampEnd, timestampStart);
            getCO2(roomID, timestampEnd, timestampStart);
            getNoise(roomID, timestampEnd, timestampStart);
            if (readSpikes) getSpikes(roomID, timestampEnd, timestampStart);
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    /**
     * Retrieves the temperature data between a certain time period from the database
     * @param roomID the room to look in
     * @param timestampEnd the end time
     * @param timestampStart the start time
     * @throws DataAccessException if database is inaccessible will throw a DataAccessException
     */
    private void getTemperatures(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<TemperatureData> temperatures = jdbcTemplate.query("SELECT * FROM temperature_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new TemperatureData(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        temperatureRecordList.addAll(temperatures);
    }

    /**
     * Retrieves the humidity data between a certain time period from the database
     * @param roomID the room to look in
     * @param timestampEnd the end time
     * @param timestampStart the start time
     * @throws DataAccessException if database is inaccessible will throw a DataAccessException
     */
    private void getHumidity(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<HumidityData> humidity = jdbcTemplate.query("SELECT * FROM humidity_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new HumidityData(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        humidityRecordList.addAll(humidity);
    }

    /**
     * Retrieves the CO2 data between a certain time period from the database
     * @param roomID the room to look in
     * @param timestampEnd the end time
     * @param timestampStart the start time
     * @throws DataAccessException if database is inaccessible will throw a DataAccessException
     */
    private void getCO2(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<CO2Data> CO2 = jdbcTemplate.query("SELECT * FROM co2_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ?",
                (rs, rowNum) -> new CO2Data(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        CO2RecordList.addAll(CO2);
    }

    /**
     * Retrieves the noise data between a certain time period from the database
     * @param roomID the room to look in
     * @param timestampEnd the end time
     * @param timestampStart the start time
     * @throws DataAccessException if database is inaccessible will throw a DataAccessException
     */
    private void getNoise(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        List<SoundData> noise = jdbcTemplate.query("SELECT * FROM noise_entry WHERE room_id = ?" +
                        "AND timestamp BETWEEN ? AND ? ORDER BY timestamp",
                (rs, rowNum) -> new SoundData(rs.getTimestamp("timestamp"), rs.getInt("value")),
                roomID, timestampEnd, timestampStart);

        noiseRecordList.addAll(noise);
    }

    public double getAverageNoise(int roomId, Timestamp from, Timestamp to) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT AVG(value) FROM noise_entry WHERE " +
                    "room_id = ? AND timestamp BETWEEN ? AND ?",
                Double.class, roomId, from, to)).orElse(0.0);
    }

    public RoomType getRoomType(int roomId) {
        int AverageNoise = getAverageNoise(roomId, ); //TODO: Finish this with Timestamps all time
        RoomType roomType;

        if(AverageNoise >= 150){
            roomType = RoomType.aula;
        } else if (AverageNoise <= 80) {
            roomType = RoomType.group_work;
        } else {
            roomType = RoomType.individual_work;
        }
        return roomType;
    }
    /**
     * Retrieves the sound spikes between a certain time period from the database
     * @param roomID the room to look in
     * @param timestampEnd the end time
     * @param timestampStart the start time
     * @throws DataAccessException if database is inaccessible will throw a DataAccessException
     */
    private void getSpikes(int roomID, Timestamp timestampEnd, Timestamp timestampStart) throws DataAccessException {
        logger.debug("Getting spikes");

        List<SoundSpike> spikes = jdbcTemplate.query("SELECT * FROM sound_spike WHERE room_id = ?" +
                        "AND start_entry BETWEEN ? AND ? ORDER BY start_entry DESC LIMIT 10",
                (rs, rowNum) -> new SoundSpike(rs.getInt("spike_id"), roomID,
                        rs.getTimestamp("start_entry"),
                        rs.getTimestamp("end_entry")),
                roomID, timestampEnd, timestampStart);

        spikes.forEach(spike -> {
            int maxAmplitude = jdbcTemplate.query("SELECT MAX(value) FROM raw_sound_entry WHERE timestamp BETWEEN ? AND ?",
                    (rs, rowNum) -> rs.getInt(1), spike.getStartTime(), spike.getEndTime()).get(0);
            spike.setPeakAmplitude(maxAmplitude);
            int numberMeasurements = jdbcTemplate.query("SELECT COUNT(value) FROM raw_sound_entry WHERE timestamp BETWEEN ? AND ?",
                      (rs, rowNum) -> rs.getInt(1), spike.getStartTime(), spike.getEndTime()).get(0);
            spike.setNumberMeasurements(numberMeasurements);
        });


        spikeRecordList.addAll(spikes);
    }

    /**
     * Gets all the data for a specific sound spike from the database
     * @param roomId the room to look for the spike in
     * @param spikeId the spike ID to look for
     * @return Returns all the data from the spike
     */
    @Override
    public List<SoundData> getSpikeData(int roomId, int spikeId){
        try {
            List<SoundData> spikeData = jdbcTemplate.query("SELECT * FROM raw_sound_entry WHERE timestamp " +
                            "BETWEEN (SELECT start_entry FROM sound_spike WHERE spike_id = ? AND room_id = ?) " +
                            "AND (SELECT end_entry FROM sound_spike WHERE spike_id = ? AND room_id = ?)",
                    (rs, rowNum) -> new SoundData(rs.getTimestamp("timestamp"), rs.getInt("value")), spikeId, roomId, spikeId, roomId);
            return spikeData;
        } catch (DataAccessException e ){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    /**
     * Gets all the rooms that belong to a specific user
     * @param userAccount the email to search for
     * @return Returns the list of rooms
     */
    @Override
    public List<Room> getUserRooms(String userAccount) {
        try {
            return jdbcTemplate.query("SELECT * FROM room WHERE account = ?",
                    (rs, rowNum) -> new Room(rs.getInt("room_id"), rs.getString("room_name"),
                            rs.getDouble("length"), rs.getDouble("width"), rs.getDouble("height")), userAccount);
        } catch (DataAccessException e) {
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    /**
     * Gets the last reading time for a specific room
     * @param roomID the room to look for
     * @return Returns the localDateTime of the last reading
     */
    @Override
    public LocalDateTime getLastReadingTime(int roomID) {
        try {
            List<TemperatureData> lastTemp = jdbcTemplate.query("SELECT * FROM temperature_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new TemperatureData(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            List<HumidityData> lastHumid = jdbcTemplate.query("SELECT * FROM humidity_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new HumidityData(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            List<CO2Data> lastCO2 = jdbcTemplate.query("SELECT * FROM co2_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new CO2Data(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            List<SoundData> lastNoise = jdbcTemplate.query("SELECT * FROM noise_entry WHERE room_id = ? ORDER BY timestamp DESC LIMIT 1",
                    (rs, rowNum) -> new SoundData(rs.getTimestamp("timestamp"), rs.getInt("value")), roomID);

            Timestamp lastTime = Timestamp.valueOf("1970-01-01 01:00:00");

            if (!lastTemp.isEmpty()) if (lastTime.compareTo(lastTemp.get(0).getTimestamp()) < 0) lastTime = lastTemp.get(0).getTimestamp();
            if (!lastHumid.isEmpty()) if (lastTime.compareTo(lastHumid.get(0).getTimestamp()) < 0) lastTime = lastHumid.get(0).getTimestamp();
            if (!lastCO2.isEmpty()) if (lastTime.compareTo(lastCO2.get(0).getTimestamp()) < 0) lastTime = lastCO2.get(0).getTimestamp();
            if (!lastNoise.isEmpty()) if (lastTime.compareTo(lastNoise.get(0).getTimestamp()) < 0) lastTime = lastNoise.get(0).getTimestamp();

            return lastTime.toLocalDateTime();
        } catch (DataAccessException e) {
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    @Override
    public void addRoom(Room room, String email) {
        try {
            jdbcTemplate.update("INSERT INTO room (room_name, account, width, length, height) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    room.getName(), email,
                    room.getWidth(), room.getLength(), room.getHeight());
        } catch (DataAccessException e){
            throw new DatabaseException("Can not connect to database", e);
        }
    }

    @Override
    public void updateRoom(int roomID, String roomName, double width, double length, double height, String userEmail) {
        try {
            logger.debug("Updating room " + roomID + "; new name = " + roomName + ", new (width, length, height) = (" + width + ", " + length + ", " + height + ")");
            jdbcTemplate.update("UPDATE room SET room_name = ?, width = ?, length = ?, height = ? WHERE room_id = ? AND account = ?",
                    roomName, width, length, height, roomID, userEmail);
        } catch (DataAccessException e){
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

    @Override
    public List<SoundData> getNoiseRecordList() {
        return noiseRecordList;
    }

    @Override
    public List<SoundSpike> getSpikeRecordList() {
        return spikeRecordList;
    }
}
