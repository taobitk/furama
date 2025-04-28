
package service;

import model.Employee;
import repository.EmployeeRepositoryImpl;
import repository.iplm.IEmployeeRepository;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService implements IEmployeeService {
    private IEmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

    @Override
    public void addEmployee(Employee employee) throws SQLException {
        employeeRepository.save(employee); // Ném lại SQLException nếu có
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        return employeeRepository.findAll();
    }

    @Override
    public boolean isPhoneTaken(String phone) throws SQLException {
        return employeeRepository.existsByPhone(phone);
    }

    @Override
    public boolean isEmailTaken(String email) throws SQLException {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public boolean isIdCardTaken(String idCard) throws SQLException {
        return employeeRepository.existsByIdCard(idCard);
    }

    @Override
    public boolean isUsernameTaken(String username) throws SQLException {
        return employeeRepository.existsByUsername(username);
    }

}