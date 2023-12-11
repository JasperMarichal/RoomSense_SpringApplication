package be.kdg.integration3.presentation.viewmodel;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DashboardViewModel {
    @NotNull
    private int roomId;
    private LocalDateTime dateTimeStart;
    @NotNull
    private int timePeriod;

    public DashboardViewModel(int roomId, LocalDateTime dateTimeStart, int timePeriod) {
        this.roomId = roomId;
        this.dateTimeStart = dateTimeStart;
        this.timePeriod = timePeriod;
    }

    public DashboardViewModel(LocalDateTime dateTimeStart, int timePeriod) {
        this.dateTimeStart = dateTimeStart;
        this.timePeriod = timePeriod;
    }

    public DashboardViewModel() {
        this.timePeriod = 30;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return "DashboardViewModel{" +
                "roomId=" + roomId +
                ", dateTimeStart=" + dateTimeStart +
                ", timePeriod=" + timePeriod +
                '}';
    }
}
