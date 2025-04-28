
package service;

import model.EducationDegree;
import repository.EducationDegreeRepository;
import repository.iplm.IEducationDegreeRepository;
import java.sql.SQLException;
import java.util.List;

public class EducationDegreeService implements IEducationDegreeService {
    private IEducationDegreeRepository educationDegreeRepository = new EducationDegreeRepository();
    @Override
    public List<EducationDegree> findAll() throws SQLException {
        return educationDegreeRepository.findAll();
    }
}