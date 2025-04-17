
package repository;

import connect.DatabaseConnection;
import model.Customer;
import repository.iplm.ICustomerRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {

    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO customer (customer_type_id, customer_name, customer_birthday, customer_gender, customer_id_card, customer_phone, customer_email, customer_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customer;"; // Tạm thời, sẽ thay bằng phân trang sau

    private static final String CHECK_EMAIL_EXISTS = "SELECT 1 FROM customer WHERE customer_email = ? LIMIT 1;";
    private static final String CHECK_PHONE_EXISTS = "SELECT 1 FROM customer WHERE customer_phone = ? LIMIT 1;";
    private static final String CHECK_IDCARD_EXISTS = "SELECT 1 FROM customer WHERE customer_id_card = ? LIMIT 1;";


    @Override
    public void save(Customer customer) {
        System.out.println("Executing INSERT: " + INSERT_CUSTOMER_SQL);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL);

            preparedStatement.setInt(1, customer.getCustomerTypeId());
            preparedStatement.setString(2, customer.getCustomerName());
            preparedStatement.setDate(3, Date.valueOf(customer.getCustomerBirthday()));
            preparedStatement.setBoolean(4, customer.isCustomerGender());
            preparedStatement.setString(5, customer.getCustomerIdCard());
            preparedStatement.setString(6, customer.getCustomerPhone());
            preparedStatement.setString(7, customer.getCustomerEmail());
            preparedStatement.setString(8, customer.getCustomerAddress());

            System.out.println("Executing PreparedStatement: " + preparedStatement);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi thực thi INSERT khách hàng: " + e.getMessage());

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException closeException) {
                System.err.println("Lỗi khi đóng tài nguyên (save): " + closeException.getMessage());
            }
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS)) {

            System.out.println("Executing query: " + preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                int typeId = rs.getInt("customer_type_id");
                String name = rs.getString("customer_name");
                LocalDate birthday = rs.getDate("customer_birthday").toLocalDate();
                boolean gender = rs.getBoolean("customer_gender");
                String idCard = rs.getString("customer_id_card");
                String phone = rs.getString("customer_phone");
                String email = rs.getString("customer_email");
                String address = rs.getString("customer_address");
                customers.add(new Customer(id, typeId, name, birthday, gender, idCard, phone, email, address));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return customers;
    }


    // --- TRIỂN KHAI CÁC PHƯƠNG THỨC KIỂM TRA MỚI ---
    @Override
    public boolean existsByEmail(String email) throws SQLException {
        System.out.println("Checking email existence for: " + email);
        return checkExistence(CHECK_EMAIL_EXISTS, email);
    }

    @Override
    public boolean existsByPhone(String phone) throws SQLException {
        System.out.println("Checking phone existence for: " + phone);
        return checkExistence(CHECK_PHONE_EXISTS, phone);
    }

    @Override
    public boolean existsByIdCard(String idCard) throws SQLException {
        System.out.println("Checking ID card existence for: " + idCard);
        return checkExistence(CHECK_IDCARD_EXISTS, idCard);
    }


    private boolean checkExistence(String sqlQuery, String value) throws SQLException {
        boolean exists = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, value);
            System.out.println("Executing existence check: " + preparedStatement); // Debug
            rs = preparedStatement.executeQuery();
            if (rs.next()) { // Chỉ cần có 1 dòng trả về là biết tồn tại
                exists = true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi kiểm tra tồn tại ("+ value +"): " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException closeException) {
                System.err.println("Lỗi khi đóng tài nguyên (checkExistence): " + closeException.getMessage());
            }
        }
        System.out.println("Existence check result for ("+ value +"): " + exists); // Debug
        return exists;
    }

}