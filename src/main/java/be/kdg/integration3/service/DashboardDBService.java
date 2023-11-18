package be.kdg.integration3.service;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.RawDataRecord;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.repository.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jdbcrepository")
public class DashboardDBService implements DashboardService{
    private final DataRepository repository;
    private int currentId = -1;

    public DashboardDBService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * If the new ID to get data for is not the same as the data that is currently loaded by the repository fetch the new data.
     * @param id The room ID to search for
     */
    private void getData(int id){
        if (currentId != id){
            currentId = id;
            repository.read(id);
        }
    }

    /**
     * Gets the list of all temperatures for the room in the database, runs getData method to retrieve data then filters to get temperatureData
     * @param id the room ID
     * @return Returns all the temperatures for that room
     */
    @Override
    public List<TemperatureData> getTemperatureList(int id) {
        getData(id);
        return repository.getRecordList().stream().filter(record -> record instanceof TemperatureData).map(rawDataRecord -> (TemperatureData) rawDataRecord).toList();
    }

    /**
     * Gets the list of all humidity for the room in the database, runs getData method to retrieve data then filters to get humidityData
     * @param id the room ID
     * @return Returns all the humidity for that room
     */
    @Override
    public List<HumidityData> getHumidityList(int id) {
        getData(id);
        return repository.getRecordList().stream().filter(record2 -> record2 instanceof HumidityData).map(rawDataRecord2 -> (HumidityData) rawDataRecord2).toList();
    }

    /**
     * Gets the list of all CO2 for the room in the database, runs getData method to retrieve data then filters to get CO2Data
     * @param id the room ID
     * @return Returns all the CO2 for that room
     */
    @Override
    public List<CO2Data> getCO2List(int id) {
        getData(id);
        return repository.getRecordList().stream().filter(record3 -> record3 instanceof CO2Data).map(rawDataRecord3 -> (CO2Data) rawDataRecord3).toList();
    }

    @Override
    public List<TemperatureData> getTemperatureList() {
        return new ArrayList<>();
    }

    @Override
    public List<HumidityData> getHumidityList() {
        return new ArrayList<>();
    }

    @Override
    public List<CO2Data> getCO2List() {
        return new ArrayList<>();
    }
}
