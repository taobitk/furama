
package repository;

import connect.DatabaseConnection;
import model.Division;
import repository.iplm.IDivisionRepository; // Import interface

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DivisionRepository implements IDivisionRepository {

    private static final String SELECT_ALL_DIVISIONS = "SELECT division_id, division_name FROM division;";

    @Override
    public List<Division> findAll() throws SQLException {
        List<Division> divisions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DIVISIONS)) {

            System.out.println(preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("division_id");
                String name = rs.getString("division_name");
                divisions.add(new Division(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách bộ phận: " + e.getMessage());
            throw e;
        }
        return divisions;
    }
}