package repository;

import connect.DatabaseConnection;
import model.User;
import repository.iplm.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {
    private static final String SELECT_USER_BY_USERNAME = "SELECT username, password FROM user WHERE username = ?;";
    @Override
    public User findByUsername(String username) {
        User user = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {

            preparedStatement.setString(1, username);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String foundUsername = rs.getString("username");
                String foundPassword = rs.getString("password");
                user = new User(foundUsername, foundPassword);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn user: " + e.getMessage());
        }
        return user;
    }

}