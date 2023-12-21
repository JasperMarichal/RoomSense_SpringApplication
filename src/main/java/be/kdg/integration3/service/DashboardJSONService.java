package be.kdg.integration3.service;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.repository.DataRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardJSONService implements DashboardService {
    private final DataRepository repository;

    public DashboardJSONService(DataRepository repository) {
        this.repository = repository;
    }


    @Override
    public void getData(int roomID, LocalDateTime startTime, LocalDateTime endTime, boolean readSpikes) {

    }

    @Override
    public List<Room> getUserRooms(String userAccount) {
        return new ArrayList<>();
    }

    @Override
    public List<TemperatureData> getTemperatureList() {
        return repository.getTemperatureRecordList();
    }

    @Override
    public List<HumidityData> getHumidityList() {
        return repository.getHumidityRecordList();
    }

    @Override
    public List<SoundSpike> getSpikeList() {
        return repository.getSpikeRecordList();
    }

    @Override
    public List<SoundData> getSpikeData(int roomId, int spikeId) {
        return repository.getSpikeData(roomId, spikeId);
    }

    @Override
    public List<CO2Data> getCO2List() {
        return repository.getCO2RecordList();
    }

    @Override
    public List<SoundData> getNoiseList() {
        return repository.getNoiseRecordList();
    }

    @Override
    public LocalDateTime getLastTime(int roomID){
        return null;
    }

    @Override
    public void addRoom(String roomName, double width, double length, double height, String userEmail) {

    }

    @Override
    public void updateRoom(int roomID, String roomName, double width, double length, double height, String userEmail) {

    }

    @Override
    public double getAverageTemperature() {
        return 0;
    }

    @Override
    public double getAverageHumidity() {
        return 0;
    }

    @Override
    public double getAverageCO2() {
        return 0;
    }

    @Override
    public RoomType getRoomType(int roomId) {
        return null;
    }
}
