package com.example.kinobackend.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String row;
    private int seatNumber;
    private boolean isSofa;
    private boolean isCowboy;
    private double priceAdjustment;

    // @ManyToOne
   // private Screening screening;
    public int getId(){
        return id;
    }
public void setId(int id){
   this.id = id;
}

public String getRow(){
        return row;
}
public void setRow(String row){
 this.row = row;
}
public int getSeatNumber(){
        return seatNumber;
}

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isSofa() {
        return isSofa;
    }

    public void setSofa(boolean sofa) {
       this.isSofa = sofa;
    }

    public boolean isCowboy() {
        return isCowboy;
    }

    public void setCowboy(boolean cowboy) {
        this.isCowboy = cowboy;
    }

    public double getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }
   // public Screening getScreening() {
  //      return screening;
  //  }

 //   public void setScreening(Screening screening) {
  //      this.screening = screening;
    }
