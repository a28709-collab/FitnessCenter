package com.svalero.fitnesscenter.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Partner implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String phone;
    private LocalDate date;
    private boolean active;

 public Partner(String username, String email, String phone, LocalDate date, boolean active){
     this.username = username;
     this.email = email;
     this.phone= phone;
     this.date= date;
     this.active= active;
 }

 public String getUsername(){return username;}
 public void setUsername (String username){this.username =username; }

 public String getEmail(){return email;}
 public void setEmail(String email){this.email= email;}

 public String getPhone(){return phone;}
 public void setPhone(String phone){this.phone= phone;}

 public LocalDate getDate(){return date;}
 public void setDate(LocalDate date){this.date= date;}

 public boolean isActive(){return active;}
 public void setActive(boolean active){this.active= active;}

    @Override
    public String toString() {
        return username + " (" + email + ")";
    }

}
