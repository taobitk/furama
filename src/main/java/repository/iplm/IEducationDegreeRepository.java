
package repository.iplm;

import model.EducationDegree;
import java.sql.SQLException;
import java.util.List;

public interface IEducationDegreeRepository {
    List<EducationDegree> findAll() throws SQLException;
}