// File: src/main/java/service/ICustomerService.java
package service;

import model.Customer;
import java.sql.SQLException; // Import SQLException
import java.util.List;

public interface ICustomerService {

    void addCustomer(Customer customer) throws SQLException; // Chỉ cần throws SQLException
    List<Customer> findAll();
    boolean isEmailTaken(String email) throws SQLException;
    boolean isPhoneTaken(String phone) throws SQLException;
    boolean isIdCardTaken(String idCard) throws SQLException;

}