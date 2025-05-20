package org.example.DAO;

import org.example.Database.DatabaseConnection;
import org.example.Object.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    public static void insertUser(Users user) {
        String sql = "INSERT INTO users (firstname, lastname, email_address, password, birth_of_date) VALUES (?, ?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstname());
            stmt.setString(2, user.getLastname());
            stmt.setString(3, user.getEmail_address());
            stmt.setString(4, user.getPassword());
            stmt.setDate(5, user.getBirthday());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean authenticate(String email, String password) {
        String sql = "SELECT password FROM users WHERE email_address = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return BCrypt.checkpw(password, storedHash); // Compare hashed
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
