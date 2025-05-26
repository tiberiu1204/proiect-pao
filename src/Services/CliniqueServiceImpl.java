package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.Clinique;
import src.Repositories.CliniqueRepo;

import java.sql.SQLException;
import java.util.ArrayList;

public class CliniqueServiceImpl implements CliniqueService {

    private final DatabaseConfiguration db;
    public CliniqueServiceImpl(DatabaseConfiguration db) {
        this.db = db;
    }
    public ArrayList<Clinique> getAllCliniques() throws SQLException {
        CliniqueRepo cliniqueRepo = new CliniqueRepo(db);
        return cliniqueRepo.readAll();
    }
}
