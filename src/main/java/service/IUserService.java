
package service;

import model.User;

public interface IUserService {
    User checkLogin(String username, String password);
}