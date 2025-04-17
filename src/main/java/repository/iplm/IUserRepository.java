package repository.iplm;

import model.User;

public interface IUserRepository {
    User findByUsername(String username);
}