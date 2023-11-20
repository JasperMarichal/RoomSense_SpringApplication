package be.kdg.integration3.service;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.Room;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.repository.DataRepository;
import be.kdg.integration3.util.exception.DatabaseException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * @param roomID The room ID to search for
     */
    @Override
    public void getData(int roomID, LocalDateTime startTime, LocalDateTime endTime){
            repository.read(roomID, startTime, endTime);
    }

    @Override
    public List<Room> getUserRooms(String userAccount) {
        return repository.getUserRooms(userAccount);
    }

    public LocalDateTime getLastTime(int roomID){
        return repository.getLastReadingTime(roomID);
    }

    /**
     * Gets the list of all temperatures for the room in the database, runs getData method to retrieve data then filters to get temperatureData
     * @return Returns all the temperatures for that room
     */
    @Override
    public List<TemperatureData> getTemperatureList() {
        return repository.getTemperatureRecordList();
    }

    /**
     * Gets the list of all humidity for the room in the database, runs getData method to retrieve data then filters to get humidityData
     * @return Returns all the humidity for that room
     */
    @Override
    public List<HumidityData> getHumidityList() {
        return repository.getHumidityRecordList();
    }

    /**
     * Gets the list of all CO2 for the room in the database, runs getData method to retrieve data then filters to get CO2Data
     * @return Returns all the CO2 for that room
     */
    @Override
    public List<CO2Data> getCO2List() {
        return repository.getCO2RecordList();
    }
}
