package service;

import model.User;
import repository.UserRepository;
import repository.iplm.IUserRepository;

public class UserService implements IUserService {
    private IUserRepository userRepository = new UserRepository();
    @Override
    public User checkLogin(String username, String password) {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB == null) {
            return null;
        }
        if (password.equals(userFromDB.getPassword())) {
            return userFromDB;
        } else {
            return null;
        }
    }
}