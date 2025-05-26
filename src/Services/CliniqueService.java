package src.Services;

import src.Entities.Clinique;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CliniqueService {
    public ArrayList<Clinique> getAllCliniques() throws SQLException;
}
