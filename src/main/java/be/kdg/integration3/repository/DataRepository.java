package be.kdg.integration3.repository;

import be.kdg.integration3.domain.RawDataRecord;

import java.util.List;

public interface DataRepository {
    void read();

    List<RawDataRecord> getRecordList();
}
