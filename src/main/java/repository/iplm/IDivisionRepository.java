
package repository.iplm;

import model.Division;
import java.sql.SQLException;
import java.util.List;

public interface IDivisionRepository {
    List<Division> findAll() throws SQLException;
}