package org.example.DAO;

import org.example.Database.DatabaseConnection;
import org.example.Object.Property_Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Property_TypeDAO {
    public static void insertProperty_Type (Property_Type type){
        String sql = "INSERT INTO property_type(type_name,description) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type.getType_name());
            stmt.setString(2, type.getDescription());

            stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public static void updatePropertyType(int id, String typeName, String description) {
        String sql = "{ CALL update_property_type(?, ?, ?) }";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, typeName);
            stmt.setString(3, description);
            stmt.execute();

            System.out.println("Property type updated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePropertyType(int id) {
        String sql = "{ CALL delete_property_type(?) }";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTypeIdByName(String typeName) {
        String sql = "SELECT type_id FROM property_type WHERE type_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("type_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
