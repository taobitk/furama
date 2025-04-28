// File: src/main/java/repository/ContractRepository.java
package repository;

import connect.DatabaseConnection;
import model.Contract;

import java.sql.*;
import java.time.LocalDateTime; // Import LocalDateTime
import java.util.ArrayList;
import java.util.List;

public class ContractRepository {

    private static final String INSERT_CONTRACT_SQL = "INSERT INTO contract " +
            "(contract_start_date, contract_end_date, contract_deposit, contract_total_money, " +
            "employee_id, customer_id, service_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

    // === THÊM SQL CHO SELECT ALL ===
    private static final String SELECT_ALL_CONTRACTS = "SELECT * FROM contract ORDER BY contract_start_date DESC;"; // Sắp xếp theo ngày bắt đầu mới nhất
    public void save(Contract contract) throws SQLException {
        System.out.println("Executing INSERT Contract: " + INSERT_CONTRACT_SQL);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(INSERT_CONTRACT_SQL);

            ps.setTimestamp(1, Timestamp.valueOf(contract.getContractStartDate()));
            ps.setTimestamp(2, Timestamp.valueOf(contract.getContractEndDate()));
            ps.setDouble(3, contract.getContractDeposit());
            ps.setDouble(4, contract.getContractTotalMoney());
            ps.setInt(5, contract.getEmployeeId());
            ps.setInt(6, contract.getCustomerId());
            ps.setInt(7, contract.getServiceId());

            System.out.println("Executing PreparedStatement: " + ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm hợp đồng: " + e.getMessage() + " (Error Code: " + e.getErrorCode() + ")");
            throw e;
        } finally {
            closeResources(null, ps, connection);
        }
    }

    public List<Contract> findAll() throws SQLException {
        List<Contract> contracts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(SELECT_ALL_CONTRACTS);
            System.out.println("Executing: " + ps); // Debug
            rs = ps.executeQuery();

            while (rs.next()) {
                Contract contract = mapResultSetToContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách hợp đồng: " + e.getMessage());
            throw e; // Ném lại lỗi
        } finally {
            closeResources(rs, ps, connection); // Đóng tài nguyên
        }
        return contracts;
    }

    private Contract mapResultSetToContract(ResultSet rs) throws SQLException {
        Contract contract = new Contract();
        contract.setContractId(rs.getInt("contract_id"));
        // Chuyển đổi Timestamp từ DB sang LocalDateTime
        Timestamp startTimestamp = rs.getTimestamp("contract_start_date");
        if (startTimestamp != null) {
            contract.setContractStartDate(startTimestamp.toLocalDateTime());
        }
        Timestamp endTimestamp = rs.getTimestamp("contract_end_date");
        if (endTimestamp != null) {
            contract.setContractEndDate(endTimestamp.toLocalDateTime());
        }
        contract.setContractDeposit(rs.getDouble("contract_deposit"));
        contract.setContractTotalMoney(rs.getDouble("contract_total_money"));
        contract.setEmployeeId(rs.getInt("employee_id"));
        contract.setCustomerId(rs.getInt("customer_id"));
        contract.setServiceId(rs.getInt("service_id"));
        return contract;
    }


    // Phương thức trợ giúp đóng tài nguyên
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

}
