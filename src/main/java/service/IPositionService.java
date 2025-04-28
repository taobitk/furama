
package service;

import model.Position;
import java.sql.SQLException;
import java.util.List;

public interface IPositionService {
    List<Position> findAll() throws SQLException;
}