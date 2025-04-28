
package service;

import model.Position;
import repository.PositionRepository;
import repository.iplm.IPositionRepository;
import java.sql.SQLException;
import java.util.List;

public class PositionService implements IPositionService {

    private IPositionRepository positionRepository = new PositionRepository();

    @Override
    public List<Position> findAll() throws SQLException {
        return positionRepository.findAll();
    }
}