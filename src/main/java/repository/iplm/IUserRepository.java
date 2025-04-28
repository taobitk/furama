// File: src/main/java/repository/iplm/IUserRepository.java
package repository.iplm; // Đảm bảo package này đúng với cấu trúc của bạn

import model.User;
import java.sql.SQLException;

public interface IUserRepository {

    User findByUsername(String username) throws SQLException;

    void save(User user) throws SQLException;
}
