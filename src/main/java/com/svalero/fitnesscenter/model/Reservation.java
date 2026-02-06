package com.svalero.fitnesscenter.model;

import java.time.LocalDate;

public class Reservation {

    private int partnerId;
    private int trainingId;
    private boolean paid;
    private float finalPrice;
    private LocalDate date;


    public Reservation(int partnerId, int trainingId, boolean paid, float finalPrice, LocalDate date){

        this.partnerId= partnerId;
        this.trainingId= trainingId;
        this.paid= paid;
        this.finalPrice= finalPrice;
        this.date= date;
    }

    public int getPartnerId(){return partnerId;}
    public void setPartnerId(int partnerId){this.partnerId= partnerId; }

    public int getTrainingId(){return trainingId;}
    public void setTrainingId(int trainingId){this.trainingId= trainingId;}

    public boolean isPaid(){return paid;}
    public void setPaid(boolean paid){this.paid= paid; }

    public float getFinalPrice(){return finalPrice;}
    public void setFinalPrice(float finalPrice){this.finalPrice= finalPrice; }

    public LocalDate getDate(){ return date;}
    public void setDate(LocalDate date){this.date= date;}


}
