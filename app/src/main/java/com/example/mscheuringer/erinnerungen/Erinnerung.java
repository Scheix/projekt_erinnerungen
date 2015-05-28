package com.example.mscheuringer.erinnerungen;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Michael on 21.05.2015.
 */
public class Erinnerung implements Serializable{
    String title;
    String note;
    String date;
    boolean erledigt;


    public Erinnerung(String title, String note, String date, boolean erledigt) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.erledigt = erledigt;
    }

    @Override
    public String toString() {
        return title+ "\n"+date.toString();
    }
}
