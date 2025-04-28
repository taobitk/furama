
package repository.iplm;

import model.Position;
import java.sql.SQLException;
import java.util.List;

public interface IPositionRepository {
    List<Position> findAll() throws SQLException;
}