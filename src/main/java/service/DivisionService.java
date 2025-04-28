
package service;

import model.Division;
import repository.DivisionRepository;
import repository.iplm.IDivisionRepository;
import java.sql.SQLException;
import java.util.List;

public class DivisionService implements IDivisionService {

    private IDivisionRepository divisionRepository = new DivisionRepository();

    @Override
    public List<Division> findAll() throws SQLException {

        return divisionRepository.findAll();
    }
}