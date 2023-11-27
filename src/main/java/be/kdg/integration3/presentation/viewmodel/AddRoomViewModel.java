package be.kdg.integration3.presentation.viewmodel;

import jakarta.validation.constraints.*;

public class AddRoomViewModel {
    @NotBlank
    @Size(min = 2, max = 30, message = "Room name must be between 2 and 30 characters.")
    private String roomName;

    @NotNull
    @Min(value = 1, message = "width must be more than 1 meter.")
    private double width;

    @NotNull
    @Min(value = 1, message = "length must be more than 1 meter.")
    private double length;

    @NotNull
    @Min(value = 1, message = "height must be more than 1 meter.")
    private double height;

    public AddRoomViewModel() {
    }

    public AddRoomViewModel(String roomName, double width, double length, double height) {
        this.roomName = roomName;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "AddRoomViewModel{" +
                "roomName='" + roomName + '\'' +
                ", width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}
