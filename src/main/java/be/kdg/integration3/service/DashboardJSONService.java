package be.kdg.integration3.service;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.repository.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jsonrepository")
public class DashboardJSONService implements DashboardService {
    private final DataRepository repository;

    public DashboardJSONService(DataRepository repository) {
        this.repository = repository;
    }


    public List<RawDataRecord> getRecordList() {
        return repository.getRecordList();
    }

    @Override
    public void getData(int roomID, LocalDateTime startTime, LocalDateTime endTime) {

    }

    @Override
    public List<Room> getUserRooms(String userAccount) {
        return new ArrayList<>();
    }

    @Override
    public List<TemperatureData> getTemperatureList() {
        return getRecordList().stream().filter(record -> record instanceof TemperatureData).map(rawDataRecord -> (TemperatureData) rawDataRecord).toList();
    }

    @Override
    public List<HumidityData> getHumidityList() {
        return getRecordList().stream().filter(record2 -> record2 instanceof HumidityData).map(rawDataRecord2 -> (HumidityData) rawDataRecord2).toList();
    }

    @Override
    public List<CO2Data> getCO2List() {
        return getRecordList().stream().filter(record3 -> record3 instanceof CO2Data).map(rawDataRecord3 -> (CO2Data) rawDataRecord3).toList();
    }

    @Override
    public LocalDateTime getLastTime(int roomID){
        return null;
    }
}
