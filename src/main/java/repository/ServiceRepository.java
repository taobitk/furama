// File: src/main/java/repository/ServiceRepository.java
package repository;

import connect.DatabaseConnection;
import model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Repository để truy cập dữ liệu Dịch Vụ (Service).
 * (Phiên bản rút gọn, không dùng Interface)
 */
public class ServiceRepository {

    private static final String INSERT_SERVICE_SQL = "INSERT INTO service " +
            "(service_name, service_area, service_cost, service_max_people, rent_type_id, service_type_id, " +
            "standard_room, description_other_convenience, pool_area, number_of_floors, free_service_included) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // === SQL CHO SELECT ALL ===
    private static final String SELECT_ALL_SERVICES = "SELECT * FROM service ORDER BY service_name;"; // Thêm ORDER BY nếu muốn

    /**
     * Lưu một dịch vụ mới vào CSDL.
     * @param service Đối tượng Service cần lưu.
     * @throws SQLException Nếu có lỗi SQL xảy ra.
     */
    public void save(Service service) throws SQLException {
        System.out.println("Executing INSERT Service: " + INSERT_SERVICE_SQL);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(INSERT_SERVICE_SQL);

            ps.setString(1, service.getServiceName());
            ps.setInt(2, service.getServiceArea());
            ps.setDouble(3, service.getServiceCost());
            ps.setInt(4, service.getServiceMaxPeople());
            ps.setInt(5, service.getRentTypeId());
            ps.setInt(6, service.getServiceTypeId());
            ps.setString(7, service.getStandardRoom());
            ps.setString(8, service.getDescriptionOtherConvenience());

            if (service.getPoolArea() != null) {
                ps.setDouble(9, service.getPoolArea());
            } else {
                ps.setNull(9, Types.DOUBLE);
            }
            if (service.getNumberOfFloors() != null) {
                ps.setInt(10, service.getNumberOfFloors());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            ps.setString(11, service.getFreeServiceIncluded());

            System.out.println("Executing PreparedStatement: " + ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm dịch vụ: " + e.getMessage());
            throw e;
        } finally {
            closeResources(null, ps, connection);
        }
    }

    /**
     * Lấy danh sách tất cả các dịch vụ.
     * @return List các đối tượng Service.
     * @throws SQLException Nếu có lỗi truy vấn CSDL.
     */
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(SELECT_ALL_SERVICES);
            System.out.println("Executing: " + ps); // Debug
            rs = ps.executeQuery();

            while (rs.next()) {
                Service service = mapResultSetToService(rs);
                services.add(service);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách dịch vụ: " + e.getMessage());
            throw e;
        } finally {
            closeResources(rs, ps, connection);
        }
        return services;
    }

    /**
     * Ánh xạ một dòng trong ResultSet thành đối tượng Service.
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceArea(rs.getInt("service_area"));
        service.setServiceCost(rs.getDouble("service_cost"));
        service.setServiceMaxPeople(rs.getInt("service_max_people"));
        service.setRentTypeId(rs.getInt("rent_type_id"));
        service.setServiceTypeId(rs.getInt("service_type_id"));
        service.setStandardRoom(rs.getString("standard_room"));
        service.setDescriptionOtherConvenience(rs.getString("description_other_convenience"));
        // Lấy giá trị có thể null
        double poolAreaDb = rs.getDouble("pool_area");
        if (!rs.wasNull()) {
            service.setPoolArea(poolAreaDb);
        }
        int numberOfFloorsDb = rs.getInt("number_of_floors");
        if (!rs.wasNull()) {
            service.setNumberOfFloors(numberOfFloorsDb);
        }
        service.setFreeServiceIncluded(rs.getString("free_service_included"));
        return service;
    }

    // Phương thức trợ giúp đóng tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    // Các phương thức khác (findById, update, delete...) sẽ thêm sau nếu cần
}
