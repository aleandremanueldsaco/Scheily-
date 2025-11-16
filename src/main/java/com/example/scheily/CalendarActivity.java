package com.example.scheily;

import java.time.ZonedDateTime;

public class CalendarActivity {

    private ZonedDateTime date;
    private String clientName;
    private int id;
    private String colorHex; // 🟢 added field for color

    // Constructor with color
    public CalendarActivity(ZonedDateTime date, String clientName, int id, String colorHex) {
        this.date = date;
        this.clientName = clientName;
        this.id = id;
        this.colorHex = colorHex;
    }

    // (Optional) Constructor without color — if you ever use it
    public CalendarActivity(ZonedDateTime date, String clientName, int id) {
        this(date, clientName, id, "#7ed6ff"); // default color
    }

    // Getters
    public ZonedDateTime getDate() {
        return date;
    }

    public String getClientName() {
        return clientName;
    }

    public int getId() {
        return id;
    }

    public String getColorHex() {
        return colorHex;
    }

    // Setters
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "date=" + date +
                ", clientName='" + clientName + '\'' +
                ", id=" + id +
                ", colorHex='" + colorHex + '\'' +
                '}';
    }
}
