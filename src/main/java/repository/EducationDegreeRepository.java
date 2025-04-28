
package repository;

import connect.DatabaseConnection;
import model.EducationDegree;
import repository.iplm.IEducationDegreeRepository; // Import interface

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EducationDegreeRepository implements IEducationDegreeRepository {

    private static final String SELECT_ALL_EDUCATION_DEGREES = "SELECT education_degree_id, education_degree_name FROM education_degree;";

    @Override
    public List<EducationDegree> findAll() throws SQLException {
        List<EducationDegree> degrees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EDUCATION_DEGREES)) {

            System.out.println(preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("education_degree_id");
                String name = rs.getString("education_degree_name");
                degrees.add(new EducationDegree(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách trình độ học vấn: " + e.getMessage());
            throw e; // Ném lại lỗi
        }
        return degrees;
    }
}