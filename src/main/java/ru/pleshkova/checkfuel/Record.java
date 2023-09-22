package ru.pleshkova.checkfuel;

import java.util.Date;

public class Record {
    private Date date;
    private double km;
    private double kmOnLitresBK;
    private double kmOnLitresREAL;

    public Record (Date date, double km) {
        this.date = date;
        this.km = km;
    }

    public Record (Date date, double km, double kmOnLitresBK) {
        this(date, km);
        this.kmOnLitresBK = kmOnLitresBK;
    }

    public Date getDate(){
        return date;
    }

    public double getKm() {
        return km;
    }

    public double getKmOnLitresBK() {
        return kmOnLitresBK;
    }

    public double getKmOnLitresREAL() {
        return kmOnLitresREAL;
    }
}
