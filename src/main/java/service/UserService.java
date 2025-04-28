
package service; // Hoặc service.impl

import model.User;
import repository.UserRepository;
import repository.iplm.IUserRepository;
import java.sql.SQLException; // Import SQLException

public class UserService implements IUserService {

    private IUserRepository userRepository = new UserRepository();

    @Override
    public User checkLogin(String username, String password) throws SQLException {
        User userFromDB = userRepository.findByUsername(username);

        if (userFromDB == null) {
            return null;
        }

        if (password != null && password.equals(userFromDB.getPassword())) {
            return userFromDB;
        } else {
            return null;
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {

        return userRepository.findByUsername(username);
    }
}
