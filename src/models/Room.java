package models;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room {
    private int id;
    private String name;
    private String ipAddress;

    private static List<String> users = new ArrayList<String>();

    public Room(int id, String name, String ipAddress) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addUser(String name) {
        users.add(name);
    }

    public String getAllUsers() {
        String returnUsers = ";";
        for (int i = 0; i < users.size(); i++) {
            returnUsers += users.get(i) + ",";
        }
        return  returnUsers;
    }


    @Override
    public String toString() {
        return
                "id=" + id +
                ",name=" + name +
                ",ipAddress=" + ipAddress +
                ';';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id && ipAddress.equals(room.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ipAddress);
    }

}
