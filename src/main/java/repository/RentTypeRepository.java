
package repository;

import connect.DatabaseConnection;
import model.RentType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RentTypeRepository {

    private static final String SELECT_ALL_RENT_TYPES = "SELECT rent_type_id, rent_type_name, rent_type_cost FROM rent_type ORDER BY rent_type_name;";

    public List<RentType> findAll() throws SQLException {
        List<RentType> rentTypes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(SELECT_ALL_RENT_TYPES);
            System.out.println("Executing: " + ps); // Debug
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("rent_type_id");
                String name = rs.getString("rent_type_name");
                double cost = rs.getDouble("rent_type_cost");
                rentTypes.add(new RentType(id, name, cost));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách kiểu thuê: " + e.getMessage());
            throw e; // Ném lại lỗi
        } finally {
            closeResources(rs, ps, connection); // Đóng tài nguyên
        }
        return rentTypes;
    }

    // Phương thức trợ giúp đóng tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}
