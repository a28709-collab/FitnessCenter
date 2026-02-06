package com.svalero.fitnesscenter.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Training implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String coach;
    private int duration;
    private float price;
    private LocalDate date;
    private boolean available;

    public Training(int id, String name, String coach, int duration, float price, LocalDate date, boolean available) {
        this.id = id;
        this.name = name;
        this.coach = coach;
        this.duration = duration;
        this.price = price;
        this.date = date;
        this.available = available;
    }

    // Constructor sin id (DataRepository lo asigna)
    public Training(String name, String coach, int duration, float price, LocalDate date, boolean available) {
        this(0, name, coach, duration, price, date, available);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCoach() { return coach; }
    public void setCoach(String coach) { this.coach = coach; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return name + " (" + coach + ")";
    }
}
