package src.Entities;

import java.util.ArrayList;

public class Cliniuque {
    private String name;
    private String address;
    ArrayList<Room> rooms;

    public Cliniuque(String name, String address, ArrayList<Room> rooms) {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }
}
