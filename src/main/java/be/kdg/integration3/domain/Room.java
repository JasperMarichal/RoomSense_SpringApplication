package be.kdg.integration3.domain;

public class Room {
    private int id;
    private String name;
    private double length;
    private double width;
    private double height;

    public Room(int id, String name, double length, double width, double height) {
        this.id = id;
        this.name = name;
        this.length = Math.round(length * 100.0)/100.0;
        this.width = Math.round(width * 100.0)/100.0;
        this.height = Math.round(height * 100.0)/100.0;
    }

    public Room(String name, double length, double width, double height) {
        this.name = name;
        this.length = Math.round(length * 100.0)/100.0;
        this.width = Math.round(width * 100.0)/100.0;
        this.height = Math.round(height * 100.0)/100.0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
