package be.kdg.integration3.service;

import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.repository.RawDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    private final RawDataRepository repository;

    public DashboardService(RawDataRepository repository) {
        this.repository = repository;
    }

    public List<RawDataRecord> getRecordList() {
        return repository.getRecordList();
    }

    public List<TemperatureData> getTemperatureList() {
        return getRecordList().stream().filter(record -> record instanceof TemperatureData).map(rawDataRecord -> (TemperatureData) rawDataRecord).toList();
    }

    public List<HumidityData> getHumidityList() {
        return getRecordList().stream().filter(record2 -> record2 instanceof HumidityData).map(rawDataRecord2 -> (HumidityData) rawDataRecord2).toList();
    }
}
