// File: src/main/java/repository/ServiceTypeRepository.java
package repository;

import connect.DatabaseConnection; // Import lớp kết nối
import model.ServiceType; // Import model tương ứng

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTypeRepository {

    private static final String SELECT_ALL_SERVICE_TYPES = "SELECT service_type_id, service_type_name FROM service_type ORDER BY service_type_name;";


    public List<ServiceType> findAll() throws SQLException {
        List<ServiceType> serviceTypes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(SELECT_ALL_SERVICE_TYPES);
            System.out.println("Executing: " + ps); // Debug
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("service_type_id");
                String name = rs.getString("service_type_name");
                serviceTypes.add(new ServiceType(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách loại dịch vụ: " + e.getMessage());
            throw e; // Ném lại lỗi
        } finally {
            closeResources(rs, ps, connection); // Đóng tài nguyên
        }
        return serviceTypes;
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}
