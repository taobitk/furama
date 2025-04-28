
package repository;

import connect.DatabaseConnection;
import model.Position;
import repository.iplm.IPositionRepository; // Import interface

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository implements IPositionRepository {

    private static final String SELECT_ALL_POSITIONS = "SELECT position_id, position_name FROM position;";

    @Override
    public List<Position> findAll() throws SQLException {
        List<Position> positions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_POSITIONS)) {

            System.out.println(preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("position_id");
                String name = rs.getString("position_name");
                positions.add(new Position(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách vị trí: " + e.getMessage());
            throw e;
        }
        return positions;
    }
}