package be.kdg.integration3.repository;

import be.kdg.integration3.domain.RawDataRecord;

import java.util.List;

public interface RawDataRepository {
    int readSerial();

    List<RawDataRecord> getRecordList();
}
