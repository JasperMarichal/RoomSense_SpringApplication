package be.kdg.integration3.service;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    private final DataRepository repository;

    public DashboardService(DataRepository repository) {
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

    public List<CO2Data> getCO2List() {
        return getRecordList().stream().filter(record3 -> record3 instanceof CO2Data).map(rawDataRecord3 -> (CO2Data) rawDataRecord3).toList();
    }
}
