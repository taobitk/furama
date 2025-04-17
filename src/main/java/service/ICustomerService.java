
package service;

import model.Customer;
import java.util.List;

public interface ICustomerService {

    void addCustomer(Customer customer);
    List<Customer> findAll();

}