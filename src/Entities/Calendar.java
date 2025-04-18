package src.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Calendar {
    private int startHour;
    private int endHour;
    private ArrayList<Date> freeDaysThisYear;
    private HashMap<Integer, HashMap<Integer, HashMap<Double, Integer>>> appointedDates;

    public Calendar(int startHour, int endHour, ArrayList<Date> freeDaysThisYear,
                    HashMap<Integer, HashMap<Integer, HashMap<Double, Integer>>> appointedDates) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.freeDaysThisYear = freeDaysThisYear;
        this.appointedDates = appointedDates;
    }
}
