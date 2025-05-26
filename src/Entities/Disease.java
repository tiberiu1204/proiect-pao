package src.Entities;

import src.Utils.DiseaseType;

public class Disease {
    private int id;
    private String name;
    private DiseaseType type;

    public Disease(int id, String name, DiseaseType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public DiseaseType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
}
