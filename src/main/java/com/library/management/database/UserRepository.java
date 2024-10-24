package com.library.management.database;

import com.library.management.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public User getUser(int userID) {
        try {
            String query = "SELECT * FROM users WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            } else {
                System.err.println("User not found");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("error while retrieving user: " + e.getMessage());
            return null;
        }
    }

    public User getUser(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
            } else {
                System.err.println("User not found");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("error while retrieving user: " + e.getMessage());
            return null;
        }
}

public User getUserByCredentials(String username, String password) {
    try {
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (!rs.next() || !BCrypt.checkpw(password, rs.getString("password"))) {
            System.err.println("User not found");
            return null;
        }
        return new User(rs.getInt("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
    } catch (SQLException e) {
        System.err.println("error while retrieving user: " + e.getMessage());
        return null;
    }
}

public List<User> getUsers() {
    try {
        String query = "SELECT * FROM users WHERE role= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, "user");
        ResultSet rs = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getInt("userID"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password"), rs.getString("role")));
        }
        return users;
    } catch (SQLException e) {
        System.err.println("error while retrieving users: " + e.getMessage());
        return null;
    }
}

public boolean addUser(User user) {
    try {
        String query = "INSERT INTO users (firstName, lastName, username, password) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setString(3, user.getUsername());
        preparedStatement.setString(4, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while adding user: " + e.getMessage());
        return false;
    }
}

public boolean addUser(String username, String password) {
    try {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, hashedPassword);
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while adding user: " + e.getMessage());
        return false;
    }
}

public boolean updateUserFirstName(User user, String firstName) {
    try {
        String query = "UPDATE users SET firstName = ? WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setInt(2, user.getUserID());
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while updating user name: " + e.getMessage());
        return false;
    }
}


public boolean updateUserLastName(User user, String lastName) {
    try {
        String query = "UPDATE users SET lastName = ? WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, lastName);
        preparedStatement.setInt(2, user.getUserID());
        return preparedStatement.execute();
    } catch (SQLException e) {
        System.err.println("error while updating user last name: " + e.getMessage());
        return false;
    }
}

public boolean updateUser(User user, String firstName, String lastName) {
    try {
        String query = "UPDATE users SET firstName = ?, lastName = ? WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setInt(3, user.getUserID());
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while updating user: " + e.getMessage());
        return false;
    }
}

public boolean updateUser(User user, String firstName, String lastName, String password) {
    try {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "UPDATE users SET firstName = ?, lastName = ?, password = ? WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, hashedPassword);
        preparedStatement.setInt(4, user.getUserID());
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while updating user: " + e.getMessage());
        return false;
    }
}

public boolean deleteUser(int userID) {
    try {
        String query = "DELETE FROM users WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userID);
        return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("error while deleting user: " + e.getMessage());
        return false;
    }
}

    public boolean deleteUser(String username) {
        try {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("error while deleting user: " + e.getMessage());
            return false;
        }
    }

public Connection getConnection() {
    return connection;
}

public boolean isUsernameValid(String username) {
        try{
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        }catch(SQLException e){
            System.err.println("error while checking if username is valid: " + e.getMessage());
            return false;
        }
}
}
