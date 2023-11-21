package be.kdg.integration3.repository;

import be.kdg.integration3.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public interface DataRepository {
    void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Room> getUserRooms(String userAccount);
    List<TemperatureData> getTemperatureRecordList();
    List<HumidityData> getHumidityRecordList();
    List<CO2Data> getCO2RecordList();
    List<SoundData> getNoiseRecordList();
    LocalDateTime getLastReadingTime(int roomID);
}
