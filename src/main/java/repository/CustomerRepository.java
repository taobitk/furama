
package repository;

import connect.DatabaseConnection;
import model.Customer;
import repository.iplm.ICustomerRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository { // Implement ICustomerRepository

    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO customer (customer_type_id, customer_name, customer_birthday, customer_gender, customer_id_card, customer_phone, customer_email, customer_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customer;";
    private static final String CHECK_EMAIL_EXISTS = "SELECT 1 FROM customer WHERE customer_email = ? LIMIT 1;";
    private static final String CHECK_PHONE_EXISTS = "SELECT 1 FROM customer WHERE customer_phone = ? LIMIT 1;";
    private static final String CHECK_IDCARD_EXISTS = "SELECT 1 FROM customer WHERE customer_id_card = ? LIMIT 1;";

    @Override
    public void save(Customer customer) {
        System.out.println(INSERT_CUSTOMER_SQL); // Debug
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL)) {

            preparedStatement.setInt(1, customer.getCustomerTypeId());
            preparedStatement.setString(2, customer.getCustomerName());
            // Chuyển đổi LocalDate sang java.sql.Date
            preparedStatement.setDate(3, Date.valueOf(customer.getCustomerBirthday()));
            preparedStatement.setBoolean(4, customer.isCustomerGender());
            preparedStatement.setString(5, customer.getCustomerIdCard());
            preparedStatement.setString(6, customer.getCustomerPhone());
            preparedStatement.setString(7, customer.getCustomerEmail());
            preparedStatement.setString(8, customer.getCustomerAddress());

            System.out.println(preparedStatement); // Debug
            preparedStatement.executeUpdate(); // Dùng executeUpdate cho INSERT, UPDATE, DELETE
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS)) {

            System.out.println(preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                int typeId = rs.getInt("customer_type_id");
                String name = rs.getString("customer_name");
                // Chuyển đổi java.sql.Date sang LocalDate
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

}