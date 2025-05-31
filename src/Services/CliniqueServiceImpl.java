package src.Services;

import src.Config.DatabaseConfiguration;
import src.Entities.Clinique;
import src.Entities.Room;
import src.Repositories.CliniqueRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CliniqueServiceImpl implements CliniqueService {

    private final DatabaseConfiguration db;
    private final CliniqueRepo cliniqueRepo;
    public CliniqueServiceImpl(DatabaseConfiguration db) {
        this.db = db;
        this.cliniqueRepo = new CliniqueRepo(db);
    }
    @Override
    public ArrayList<Clinique> getAllCliniques() throws SQLException, IOException {
        AuditService.log("CliniqueService:getAllCliniques");
        return cliniqueRepo.readAll();
    }

    @Override
    public Clinique createClinique(String name, String address, ArrayList<Room> rooms) throws SQLException {
        Clinique clinique = new Clinique(-1, name, address, rooms);
        cliniqueRepo.create(clinique);
        return clinique;
    }

    @Override
    public Clinique updateCliniqueName(Clinique clinique, String name) throws SQLException {
        clinique.setName(name);
        cliniqueRepo.update(clinique);
        return clinique;
    }

    @Override
    public void removeClinique(Clinique clinique) throws SQLException {
        cliniqueRepo.delete(clinique.getId());
    }
}
