package org.udeshika.laundryroommanager.database.repositories.implementation;

import org.udeshika.laundryroommanager.database.DbConnection;
import org.udeshika.laundryroommanager.database.repositories.UserRepository;
import org.udeshika.laundryroommanager.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class UserRepositoryImpl implements UserRepository {

    Connection dbConnection;
    static Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());

    public UserRepositoryImpl() {
        dbConnection = DbConnection.getConnection();
    }

    @Override
    public boolean insert(User user) {
        try {
            PreparedStatement stmt = dbConnection.prepareStatement
                    ("INSERT INTO user(userId, name) VALUES(? ,?)");
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getName());
            return stmt.execute();
        } catch (SQLException e) {
            logger.log(INFO, String.format("DB error in inserting user: %s  %s ", user.getUserId(), e));
            return false;
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate("DELETE FROM user");
        } catch (SQLException e) {
            logger.log(INFO, "DB error in deleting the users: " + e);
        }
    }
}
