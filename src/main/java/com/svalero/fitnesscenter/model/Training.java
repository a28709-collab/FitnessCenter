package com.svalero.fitnesscenter.model;

import java.time.LocalDate;

public class Training {

    private String name;
    private String coach;
    private  int duration;
    private float price;
    private LocalDate date;
    private boolean available;

 public Training (String name, String coach, int duration, float price, LocalDate date, boolean available){
     this.name= name;
     this.coach= coach;
     this. duration= duration;
     this.price= price;
     this.date= date;
     this.available= available;
 }

 public String getName(){return name;}
 public void setName(String name){this.name= name;}

 public String getCoach(){return coach;}
 public void setCoach(String coach){this.coach=coach;}

    public int getDuration(){return duration;}
    public void setDuration(int duration){this.duration= duration;}

    public float getPrice(){return price;}
    public void setPrice(int price){this.price= price;}

    public LocalDate getDate(){return date;}
    public void setDate(LocalDate date){this.date= date;}

    public boolean isAvailableCoach(){return available;}
    public void setAvailable(boolean available){this.available= available;}

}
