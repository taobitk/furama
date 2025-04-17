
package repository.iplm;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface ICustomerRepository {
    void save(Customer customer);
    List<Customer> findAll();
    boolean existsByEmail(String email) throws SQLException;
    boolean existsByPhone(String phone) throws SQLException;
    boolean existsByIdCard(String idCard) throws SQLException;

}