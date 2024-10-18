package com.library.management.database;

import com.library.management.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public User getUser(int userID){
        try{
            String query = "SELECT * FROM users WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            return new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password"));
        }catch (SQLException e){
            System.err.println("error while retrieving user: " + e.getMessage());
            return null;
        }
    }

    public List<User> getUsers(){
        try{
            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while(rs.next()){
                users.add(new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), rs.getString("password")));
            }
            return users;
        }catch(SQLException e){
            System.err.println("error while retrieving users: " + e.getMessage());
            return null;
        }
    }

    public boolean addUser(User user){
        try {
            String query = "INSERT INTO users (firstName, lastName, username, password) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            return preparedStatement.execute();
        } catch (SQLException e) {
            System.err.println("error while adding user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(int userID, String firstName, String lastName, String username, String password) {
        User newUser = getUser(userID);
        if(newUser == null){
            return false;
        }
        if(firstName != null){
            newUser.setFirstName(firstName);
        }
        if(lastName != null){
            newUser.setLastName(lastName);
        }
        if(username != null){
            newUser.setUsername(username);
        }
        if(password != null){
            newUser.setPassword(password);
        }
        return addUser(newUser);
    }

    public boolean deleteUser(int userID){
        try {
            String query = "DELETE FROM users WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            return preparedStatement.execute();
        } catch (SQLException e) {
           System.err.println("error while deleting user: " + e.getMessage());
           return false;
        }
    }

}
