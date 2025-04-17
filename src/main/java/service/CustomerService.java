
package service;

import model.Customer;
import repository.CustomerRepository;
import repository.iplm.ICustomerRepository;

import java.util.List;

public class CustomerService implements ICustomerService {

    private ICustomerRepository customerRepository = new CustomerRepository(); // Sử dụng lớp CustomerRepository đã tạo

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {

        return customerRepository.findAll();
    }
}