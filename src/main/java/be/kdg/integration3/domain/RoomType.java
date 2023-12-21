package be.kdg.integration3.domain;

public enum RoomType {
    aula("Aula"), group_work("Group Work"), individual_work("Individual Work");

    private String description;

    RoomType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
