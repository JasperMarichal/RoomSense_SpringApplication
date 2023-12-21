package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface DataRepository {
    void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean readSpikes);

    List<SoundData> getSpikeData(int roomId, int spikeId);

    List<Room> getUserRooms(String userAccount);

    void addRoom(Room room, String email);

    void updateRoom(int roomID, String roomName, double width, double length, double height, String userEmail);

    List<TemperatureData> getTemperatureRecordList();
    List<HumidityData> getHumidityRecordList();
    List<CO2Data> getCO2RecordList();
    List<SoundData> getNoiseRecordList();
    LocalDateTime getLastReadingTime(int roomID);
    List<SoundSpike> getSpikeRecordList();
    double getAverageCo2(int roomId, Timestamp from, Timestamp to);
    double getAverageHumidity(int roomId, Timestamp from, Timestamp to);
    double getAverageTemp(int roomId, Timestamp from, Timestamp to);
}
