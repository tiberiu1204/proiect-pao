package src.Entities;

import src.Utils.RoomType;

public class Room {
    private int id;
    private RoomType type;
    private int floor;
    private Integer roomNumber;

    public Room(int id, RoomType type, int floor, int roomNumber) {
        this.id = id;
        this.type = type;
        this.floor = floor;
        this.roomNumber = roomNumber;
    }

    public int getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }

    public int getFloor() {
        return floor;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setId(int id) {
        this.id = id;
    }
}
