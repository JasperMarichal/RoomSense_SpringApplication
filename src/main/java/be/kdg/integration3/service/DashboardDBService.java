package be.kdg.integration3.service;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.repository.DataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardDBService implements DashboardService{
    private final DataRepository repository;

    public DashboardDBService(DataRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetches the data for a specific room between a specific time period
     * @param roomID the room to search in
     * @param startTime the time of first entry to search for
     * @param endTime the time of last entry to search for
     */
    @Override
    public void getData(int roomID, LocalDateTime startTime, LocalDateTime endTime, boolean readSpikes){
            repository.read(roomID, startTime, endTime, readSpikes);
    }

    /**
     * Fetches all the rooms that belong to a certain user
     * @param userAccount the email of the account to search for
     * @return the list of rooms
     */
    @Override
    public List<Room> getUserRooms(String userAccount) {
        return repository.getUserRooms(userAccount);
    }

    /**
     * Gets the last recorded time for a specific room
     * @param roomID the roomID
     * @return returns the datetime of the last reading
     */
    @Override
    public LocalDateTime getLastTime(int roomID){
        return repository.getLastReadingTime(roomID);
    }

    @Override
    public void addRoom(String roomName, double width, double length, double height, String userEmail) {
        repository.addRoom(new Room(roomName, width, length, height), userEmail);
    }

    @Override
    public double getAverageTemperature() {
        double totalTemp = repository.getTemperatureRecordList().stream().mapToInt(TemperatureData::getValue).sum();
        return totalTemp / repository.getTemperatureRecordList().size();
    }

    @Override
    public double getAverageHumidity() {
        double totalHumidity = repository.getHumidityRecordList().stream().mapToInt(HumidityData::getValue).sum();
        return totalHumidity / repository.getHumidityRecordList().size();
    }

    @Override
    public double getAverageCO2() {
        double totalCO2 = repository.getCO2RecordList().stream().mapToInt(CO2Data::getValue).sum();
        return totalCO2 / repository.getCO2RecordList().size();
    }

    /**
     * Gets the list of all temperatures currently loaded in the database
     * @return Returns all the temperatures for that room
     */
    @Override
    public List<TemperatureData> getTemperatureList() {
        return repository.getTemperatureRecordList();
    }

    /**
     * Gets the list of all humidity currently loaded in the database
     * @return Returns all the humidity for that room
     */
    @Override
    public List<HumidityData> getHumidityList() {
        return repository.getHumidityRecordList();
    }

    /**
     * Gets the list of all noise currently loaded in the database
     * @return Returns all the noise entries for that room
     */
    @Override
    public List<SoundData> getNoiseList() {
        return repository.getNoiseRecordList();
    }

    /**
     * Gets the list of all the spikes currently loaded in the database
     * @return Returns all the SoundSpikes for that room
     */
    @Override
    public List<SoundSpike> getSpikeList() {return  repository.getSpikeRecordList();}

    /**
     * Gets all the soundData belonging to a spike
     * @param roomId the roomID to search for the spike in
     * @param spikeId the spikeID to search for
     * @return Returns all the sound data within that spike
     */
    @Override
    public List<SoundData> getSpikeData(int roomId, int spikeId) {return repository.getSpikeData(roomId, spikeId);}

    /**
     * Gets the list of all CO2 currently loaded in the database
     * @return Returns all the CO2 for that room
     */
    @Override
    public List<CO2Data> getCO2List() {
        return repository.getCO2RecordList();
    }
}
