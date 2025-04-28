
package service;

import model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeService {

    void addEmployee(Employee employee) throws SQLException;
    List<Employee> findAll() throws SQLException;
    boolean isPhoneTaken(String phone) throws SQLException;
    boolean isEmailTaken(String email) throws SQLException;
    boolean isIdCardTaken(String idCard) throws SQLException;
    boolean isUsernameTaken(String username) throws SQLException;
}