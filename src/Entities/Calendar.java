package src.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Calendar {
    private int medicId;
    private int startHour;
    private int endHour;
    private ArrayList<Date> freeDaysThisYear;
    private HashMap<Integer, HashMap<Integer, Double>> appointedDates;

    public Calendar(int medicId, int startHour, int endHour, ArrayList<Date> freeDaysThisYear,
                    HashMap<Integer, HashMap<Integer, Double>> appointedDates) {
        this.medicId = medicId;
        this.startHour = startHour;
        this.endHour = endHour;
        this.freeDaysThisYear = freeDaysThisYear;
        this.appointedDates = appointedDates;
    }
}
