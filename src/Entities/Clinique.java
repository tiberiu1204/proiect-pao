package src.Entities;

import java.util.ArrayList;

public class Clinique {
    private int id;
    private String name;
    private String address;
    private ArrayList<Room> rooms;

    public Clinique(int id, String name, String address, ArrayList<Room> rooms) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return name + ", " + address;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
