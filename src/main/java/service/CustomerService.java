
package service;

import model.Customer;
import repository.CustomerRepository;
import repository.iplm.ICustomerRepository;
import java.sql.SQLException; // Import
import java.util.ArrayList;
import java.util.List;

public class CustomerService implements ICustomerService {

    private ICustomerRepository customerRepository = new CustomerRepository();

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            System.err.println("Lỗi ở Service khi gọi findAll Customer: "+ e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isEmailTaken(String email) throws SQLException {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneTaken(String phone) throws SQLException {
        return customerRepository.existsByPhone(phone);
    }

    @Override
    public boolean isIdCardTaken(String idCard) throws SQLException {
        return customerRepository.existsByIdCard(idCard);
    }
}