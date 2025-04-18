
package src.Repository;

import src.Entities.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CalendarRepo {
    private ArrayList<Calendar> calendars;

    public CalendarRepo() {
        this.calendars = new ArrayList<>();
        this.fetchCalendars();
    }

    private void fetchCalendars() {
        ArrayList<Date> freeDays1 = new ArrayList<>();
        freeDays1.add(new Date());

        ArrayList<Date> freeDays2 = new ArrayList<>();
        freeDays2.add(new Date());

        ArrayList<Date> freeDays3 = new ArrayList<>();
        freeDays3.add(new Date());

        HashMap<Integer, HashMap<Integer, HashMap<Double, Integer>>> appointedDates1 = new HashMap<>();
        appointedDates1.put(1, new HashMap<>());
        appointedDates1.get(1).put(5, new HashMap<>());
        appointedDates1.get(1).get(5).put(9.0, 30);

        HashMap<Integer, HashMap<Integer, HashMap<Double, Integer>>> appointedDates2 = new HashMap<>();
        appointedDates2.put(2, new HashMap<>());
        appointedDates2.get(2).put(6, new HashMap<>());
        appointedDates2.get(2).get(6).put(10.0, 45);

        HashMap<Integer, HashMap<Integer, HashMap<Double, Integer>>> appointedDates3 = new HashMap<>();
        appointedDates3.put(3, new HashMap<>());
        appointedDates3.get(3).put(7, new HashMap<>());
        appointedDates3.get(3).get(7).put(11.5, 60);

        Calendar calendar1 = new Calendar(8, 16, freeDays1, appointedDates1);
        Calendar calendar2 = new Calendar(9, 17, freeDays2, appointedDates2);
        Calendar calendar3 = new Calendar(10, 18, freeDays3, appointedDates3);

        ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(calendar1);
        calendars.add(calendar2);
        calendars.add(calendar3);
    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }
}
    