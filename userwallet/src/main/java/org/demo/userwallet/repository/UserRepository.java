package org.demo.userwallet.repository;

import org.demo.userwallet.model.User;
import org.demo.userwallet.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());

    public boolean userExistByEmailOrPhone(String email, String phone){
        String sql = "select id from users where email = ? or phone = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, phone);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while finding user by email or phone", e);
        }
        return false;
    }

    public Optional<Long> findUserIdByPhone(String phone){
        String sql = "select id from users where phone = ?";
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(resultSet.getLong("id"));
            }
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error while finding user by phone", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email){
        String sql = "select * from users where email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getDouble("wallet_balance"),
                        resultSet.getBoolean("status"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while finding user by email", e);
        }
        return Optional.empty();
    }

    public List<User> findAll(){
        String sql = "select id, name, phone from users where status = true";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while finding all users", e);
        }
        return Collections.emptyList();
    }

    public User save(User user){
        String sql = "insert into users (name, email, password, phone, address, wallet_balance) values (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getAddress());
            statement.setDouble(6, user.getWalletBalance());
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while saving user", e);
        }
        return user;
    }
}