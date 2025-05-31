package src.Services;

import src.Entities.Clinique;
import src.Entities.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CliniqueService {
    public ArrayList<Clinique> getAllCliniques() throws SQLException, IOException;
    public Clinique createClinique(String name, String address, ArrayList<Room> rooms) throws SQLException;
    public Clinique updateCliniqueName(Clinique clinique, String name) throws SQLException;
    public void removeClinique(Clinique clinique) throws SQLException;
}
