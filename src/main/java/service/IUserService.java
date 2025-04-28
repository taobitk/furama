// File: src/main/java/service/IUserService.java
package service;

import model.User;
import java.sql.SQLException; // Import SQLException

public interface IUserService {

    User checkLogin(String username, String password) throws SQLException;
    void addUser(User user) throws SQLException; // <<<=== THÊM PHƯƠNG THỨC NÀY
    User findUserByUsername(String username) throws SQLException; // <<<=== THÊM PHƯƠNG THỨC NÀY (hoặc dùng checkLogin nếu logic phù hợp)
}
