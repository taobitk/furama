
package repository;

import connect.DatabaseConnection;
import model.Employee;
import repository.iplm.IEmployeeRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements IEmployeeRepository {

    // SQL Constants
    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO employee (employee_name, employee_birthday, employee_id_card, employee_salary, employee_phone, employee_email, employee_address, position_id, education_degree_id, division_id, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM employee;";
    private static final String CHECK_PHONE_EXISTS = "SELECT 1 FROM employee WHERE employee_phone = ? LIMIT 1;";
    private static final String CHECK_EMAIL_EXISTS = "SELECT 1 FROM employee WHERE employee_email = ? LIMIT 1;";
    private static final String CHECK_IDCARD_EXISTS = "SELECT 1 FROM employee WHERE employee_id_card = ? LIMIT 1;";
    private static final String CHECK_USERNAME_EXISTS = "SELECT 1 FROM employee WHERE username = ? LIMIT 1;";

    @Override
    public void save(Employee employee) throws SQLException {
        System.out.println("Executing INSERT Employee: " + INSERT_EMPLOYEE_SQL);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(INSERT_EMPLOYEE_SQL);

            ps.setString(1, employee.getEmployeeName());
            // Kiểm tra null cho ngày sinh trước khi chuyển đổi
            if (employee.getEmployeeBirthday() != null) {
                ps.setDate(2, Date.valueOf(employee.getEmployeeBirthday()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setString(3, employee.getEmployeeIdCard());
            ps.setDouble(4, employee.getEmployeeSalary());
            ps.setString(5, employee.getEmployeePhone());
            ps.setString(6, employee.getEmployeeEmail());
            ps.setString(7, employee.getEmployeeAddress());
            ps.setInt(8, employee.getPositionId());
            ps.setInt(9, employee.getEducationDegreeId());
            ps.setInt(10, employee.getDivisionId());
            ps.setString(11, employee.getUsername());

            System.out.println("Executing PreparedStatement: " + ps);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thêm nhân viên: " + e.getMessage());
            throw e;
        } finally {
            closeResources(null, ps, connection);
        }
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        System.out.println("Executing SELECT ALL Employees: " + SELECT_ALL_EMPLOYEES);
        List<Employee> employees = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(SELECT_ALL_EMPLOYEES);
            rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách nhân viên: " + e.getMessage());
            throw e;
        } finally {
            closeResources(rs, ps, connection);
        }
        return employees;
    }

    // --- Triển khai các phương thức kiểm tra tồn tại ---
    @Override
    public boolean existsByPhone(String phone) throws SQLException {
        return checkExistence(CHECK_PHONE_EXISTS, phone);
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        return checkExistence(CHECK_EMAIL_EXISTS, email);
    }

    @Override
    public boolean existsByIdCard(String idCard) throws SQLException {
        return checkExistence(CHECK_IDCARD_EXISTS, idCard);
    }

    @Override
    public boolean existsByUsername(String username) throws SQLException {
        return checkExistence(CHECK_USERNAME_EXISTS, username);
    }



    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        int id = rs.getInt("employee_id");
        String name = rs.getString("employee_name");
        Date birthdaySql = rs.getDate("employee_birthday");
        LocalDate birthday = (birthdaySql != null) ? birthdaySql.toLocalDate() : null;
        String idCard = rs.getString("employee_id_card");
        double salary = rs.getDouble("employee_salary");
        String phone = rs.getString("employee_phone");
        String email = rs.getString("employee_email");
        String address = rs.getString("employee_address");
        int positionId = rs.getInt("position_id");
        int educationDegreeId = rs.getInt("education_degree_id");
        int divisionId = rs.getInt("division_id");
        String username = rs.getString("username");

        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setEmployeeName(name);
        employee.setEmployeeBirthday(birthday);
        employee.setEmployeeIdCard(idCard);
        employee.setEmployeeSalary(salary);
        employee.setEmployeePhone(phone);
        employee.setEmployeeEmail(email);
        employee.setEmployeeAddress(address);
        employee.setPositionId(positionId);
        employee.setEducationDegreeId(educationDegreeId);
        employee.setDivisionId(divisionId);
        employee.setUsername(username);
        return employee;
    }

    private boolean checkExistence(String sqlQuery, String value) throws SQLException {
        boolean exists = false;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            ps = connection.prepareStatement(sqlQuery);
            ps.setString(1, value);
            System.out.println("Executing existence check: " + ps); // Debug
            rs = ps.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi kiểm tra tồn tại ("+ value +"): " + e.getMessage());
            throw e;
        } finally {
            closeResources(rs, ps, connection);
        }
        System.out.println("Existence check result for ("+ value +"): " + exists); // Debug
        return exists;
    }

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Các phương thức khác sẽ thêm sau ---

}