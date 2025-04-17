
package repository;

import connect.DatabaseConnection;
import model.CustomerType;
import repository.iplm.ICustomerTypeRepository; // Import đúng interface

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerTypeRepository implements ICustomerTypeRepository {

    private static final String SELECT_ALL_CUSTOMER_TYPES = "SELECT customer_type_id, customer_type_name FROM customer_type;";

    @Override
    public List<CustomerType> findAll() {
        List<CustomerType> customerTypes = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMER_TYPES)) {

            System.out.println(preparedStatement); // Debug
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("customer_type_id");
                String name = rs.getString("customer_type_name");
                customerTypes.add(new CustomerType(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách loại khách hàng: " + e.getMessage());
        }
        return customerTypes;
    }
}