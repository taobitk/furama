
package service;

import model.EducationDegree;
import java.sql.SQLException;
import java.util.List;

public interface IEducationDegreeService {
    List<EducationDegree> findAll() throws SQLException;
}