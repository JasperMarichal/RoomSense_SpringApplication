package be.kdg.integration3.presentation.viewmodel;

import jakarta.validation.constraints.*;

public class AddRoomViewModel {
    @NotBlank(message = "Room name cannot be blank")
    @Size(min = 2, max = 30, message = "Room name must be between 2 and 30 characters.")
    private String roomName;

    @NotNull(message = "Width must not be null")
    @Pattern(regexp = "\\d+(.\\d)?", message = "value must be a number")
    @Min(value = 1, message = "Width must be more than or equal to 1 meter.")
    private String width;

    @NotNull(message = "Length must not be null")
    @Pattern(regexp = "\\d+(.\\d)?", message = "value must be a number")
    @Min(value = 1, message = "Length must be more than or equal to 1 meter.")
    private String length;

    @NotNull(message = "Height must not be null")
    @Pattern(regexp = "\\d+(.\\d)?", message = "value must be a number")
    @Min(value = 1, message = "Height must be more than or equal to 1 meter.")
    private String height;

    public AddRoomViewModel() {
    }

    public AddRoomViewModel(String roomName, String width, String length, String height) {
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
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
