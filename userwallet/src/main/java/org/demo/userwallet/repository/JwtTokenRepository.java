package org.demo.userwallet.repository;

import org.demo.userwallet.model.JwtToken;
import org.demo.userwallet.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtTokenRepository {
    private static final Logger logger = Logger.getLogger(JwtTokenRepository.class.getName());

    public Optional<JwtToken> findTokenByToken(String token){
        String sql = "select token, user_id from jwt_sessions where token = ?";
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return Optional.of(
                        new JwtToken(
                                resultSet.getString("token"),
                                resultSet.getLong("user_id")
                        )
                );
            }
        }catch (SQLException e){
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return Optional.empty();
    }

    public void save(String token, Long id){
        String sql = "insert into jwt_sessions(token, user_id) values (?, ?)";
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, token);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            logger.log(Level.SEVERE,"error while saving the token", e);
        }
    }

    public void delete(String token){
        String sql = "delete from jwt_sessions where token = ?";
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
           preparedStatement.setString(1, token);
           preparedStatement.executeUpdate();
        }catch (SQLException e){
            logger.log(Level.SEVERE,"error while deleting the token", e);
        }
    }
}
