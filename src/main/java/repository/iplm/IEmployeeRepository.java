
package repository.iplm;

import model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeRepository {
    void save(Employee employee) throws SQLException;
    List<Employee> findAll() throws SQLException;
    boolean existsByPhone(String phone) throws SQLException;
    boolean existsByEmail(String email) throws SQLException;
    boolean existsByIdCard(String idCard) throws SQLException;
    boolean existsByUsername(String username) throws SQLException;
}