
package repository.iplm;

import model.Customer;
import java.util.List;

public interface ICustomerRepository {
    void save(Customer customer);
    List<Customer> findAll();

}