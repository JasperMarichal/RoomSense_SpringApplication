package be.kdg.integration3.repository;

import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.Room;

import java.time.LocalDateTime;
import java.util.List;

public interface DataRepository {
    void read(int roomID, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Room> getUserRooms(String userAccount);
    List<RawDataRecord> getRecordList();
    LocalDateTime getLastReadingTime(int roomID);
}
