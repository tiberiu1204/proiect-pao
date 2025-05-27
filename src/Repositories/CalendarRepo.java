package src.Repositories;

import src.Config.DatabaseConfiguration;
import src.Entities.Calendar;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class CalendarRepo {
    private final DatabaseConfiguration db;
    public CalendarRepo(DatabaseConfiguration dbConfig) {
        this.db = dbConfig;
    }

    /** Create a new Calendar row and set its generated ID back into the object */
    public void create(Calendar cal) throws SQLException {
        String sql = "INSERT INTO calendars "
                + "(start_hour, end_hour, free_days, appointed_dates) "
                + "VALUES (?,?,?,?)";
        Connection c = db.getConnection();
        try ( PreparedStatement ps = c.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS) ) {
            ps.setInt(1, cal.getStartHour());
            ps.setInt(2, cal.getEndHour());
            // free days as CSV: "1,15,23"
            String freeCsv = String.join(",",
                    cal.getFreeDaysThisYear().stream()
                            .map(Object::toString).toArray(String[]::new)
            );
            ps.setString(3, freeCsv);
            // appointed_dates as semicolon‐separated "2025-06-06T10:30#30;2025-06-07T11:00#45"
            String apptCsv = String.join(";",
                    cal.getAppointedDates().entrySet().stream()
                            .map(e -> e.getKey().toString() + "#" + e.getValue())
                            .toArray(String[]::new)
            );
            ps.setString(4, apptCsv);

            ps.executeUpdate();
            try ( ResultSet keys = ps.getGeneratedKeys() ) {
                if (keys.next()) {
                    cal.setId(keys.getInt(1));
                }
            }
        } finally {
            db.close(c);
        }
    }

    /** Read all calendars */
    public ArrayList<Calendar> readAll() throws SQLException {
        String sql = "SELECT * FROM calendars";
        ArrayList<Calendar> out = new ArrayList<>();
        Connection c = db.getConnection();
        try ( PreparedStatement ps = c.prepareStatement(sql);
              ResultSet rs = ps.executeQuery() ) {
            while (rs.next()) {
                int      id      = rs.getInt("id");
                int      start   = rs.getInt("start_hour");
                int      end     = rs.getInt("end_hour");
                String   freeCsv = rs.getString("free_days");
                ArrayList<Integer> freeDays = new ArrayList<>();
                if (freeCsv != null && !freeCsv.isEmpty()) {
                    for (String s : freeCsv.split(",")) {
                        freeDays.add(Integer.valueOf(s));
                    }
                }
                String apptCsv = rs.getString("appointed_dates");
                HashMap<LocalDateTime,Integer> appts = new HashMap<>();
                if (apptCsv != null && !apptCsv.isEmpty()) {
                    for (String token : apptCsv.split(";")) {
                        String[] parts = token.split("#");
                        LocalDateTime dt = LocalDateTime.parse(parts[0]);
                        Integer dur = Integer.valueOf(parts[1]);
                        appts.put(dt, dur);
                    }
                }
                Calendar cal = new Calendar(id, start, end, freeDays, appts);
                out.add(cal);
            }
        } finally {
            db.close(c);
        }
        return out;
    }

    /** Read one by its PK */
    public Calendar readById(int id) throws SQLException {
        String sql = "SELECT * FROM calendars WHERE id = ?";
        Connection c = db.getConnection();
        try ( PreparedStatement ps = c.prepareStatement(sql) ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                // reuse the parsing logic from readAll
                return readAll().stream()
                        .filter(cal -> cal.getId() == id)
                        .findFirst().orElse(null);
            }
        } finally {
            db.close(c);
        }
    }

    /** Update an existing calendar */
    public void update(Calendar cal) throws SQLException {
        String sql = "UPDATE calendars SET "
                + "start_hour=?, end_hour=?, free_days=?, appointed_dates=? "
                + "WHERE id=?";
        Connection c = db.getConnection();
        try ( PreparedStatement ps = c.prepareStatement(sql) ) {
            ps.setInt(1, cal.getStartHour());
            ps.setInt(2, cal.getEndHour());
            String freeCsv = String.join(",",
                    cal.getFreeDaysThisYear().stream()
                            .map(Object::toString).toArray(String[]::new)
            );
            ps.setString(3, freeCsv);
            String apptCsv = String.join(";",
                    cal.getAppointedDates().entrySet().stream()
                            .map(e -> e.getKey().toString() + "#" + e.getValue())
                            .toArray(String[]::new)
            );
            ps.setString(4, apptCsv);
            ps.setInt(5, cal.getId());
            ps.executeUpdate();
        } finally {
            db.close(c);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM calendars WHERE id = ?";
        Connection c = db.getConnection();
        try ( PreparedStatement ps = c.prepareStatement(sql) ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            db.close(c);
        }
    }
}
