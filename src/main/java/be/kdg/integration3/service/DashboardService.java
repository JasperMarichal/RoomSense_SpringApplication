package be.kdg.integration3.service;

import be.kdg.integration3.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {
    void getData(int roomID, LocalDateTime startTime, LocalDateTime endTime, boolean readSpikes);

    List<Room> getUserRooms(String userAccount);

    List<TemperatureData> getTemperatureList();

    List<HumidityData> getHumidityList();

    List<SoundSpike> getSpikeList();

    List<SoundData> getSpikeData(int roomId, int spikeId);

    List<CO2Data> getCO2List();
    List<SoundData> getNoiseList();

    LocalDateTime getLastTime(int roomID);

    void addRoom(String roomName, double width, double length, double height, String userEmail);
    double getAverageTemperature();
    double getAverageHumidity();
    double getAverageCO2();
}
