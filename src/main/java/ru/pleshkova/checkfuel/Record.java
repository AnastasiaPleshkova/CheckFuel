package ru.pleshkova.checkfuel;

import java.util.Date;
import java.util.List;

public class Record {
    private Date date;
    private double litres;
    private int km;
    private double kmOnLitresBK;
    private double kmOnLitresREAL;

    public String toLine(){
        return String.format("Дата %s. Пройдено %s км, использовано %s литров. " +
                "Расход по БК - %s, расход реальный - %s \n", this.date, this.km, this.litres,this.kmOnLitresBK, this.kmOnLitresREAL);
    }


    public Record (Date date, int km, double litres, double kmOnLitresBK) {
        this.date = date;
        this.km = km;
        this.litres = litres;
        this.kmOnLitresBK = kmOnLitresBK;
    }

    public Record (Date date, int km, double litres, double kmOnLitresBK, double kmOnLitresREAL) {
        this(date, km, litres, kmOnLitresBK);
        this.kmOnLitresREAL = kmOnLitresREAL;
    }


    public Date getDate(){
        return date;
    }

    public int getKm() {
        return km;
    }

    public double getLitres() {
        return litres;
    }

    public double getKmOnLitresBK() {
        return kmOnLitresBK;
    }

    public double getKmOnLitresREAL() {
        return kmOnLitresREAL;
    }

    public static Record getLastRecord(List<Record> list){
        Record lastRecord = list.get(0);
        for (Record record : list) {
            if (lastRecord.getDate().compareTo(record.getDate())<0){
                lastRecord = record;
            }
        }
        return lastRecord;
    }
}
