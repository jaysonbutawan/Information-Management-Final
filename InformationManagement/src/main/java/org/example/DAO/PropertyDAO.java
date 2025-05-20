package org.example.DAO;

import org.example.Database.DatabaseConnection;
import org.example.Object.Property;
import org.example.Object.PropertyDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {
    public static boolean insertProperty(Property property) {
        String sql = "INSERT INTO properties(property_name, address, size, rent_price,type_id, description) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getProperty_name());
            stmt.setString(2, property.getAddress());
            stmt.setFloat(3, property.getSize());
            stmt.setDouble(4, property.getRent_price());
            stmt.setInt(5, property.getType_id());
            stmt.setString(6, property.getDescription());

            int rowsaffected = stmt.executeUpdate();
            return rowsaffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateProperty(Property property) {
        String sql = "{CALL update_property_type(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, property.getId());
            stmt.setString(2, property.getProperty_name());
            stmt.setString(3, property.getAddress());
            stmt.setFloat(4, property.getSize());
            stmt.setDouble(5, property.getRent_price());
            stmt.setInt(6, property.getType_id());
            stmt.setString(7, property.getDescription());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean propertyExists(String propertyName) {
        String query = "SELECT COUNT(*) FROM properties WHERE property_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, propertyName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<PropertyDetails> fetchAll() {
        List<PropertyDetails> properties = new ArrayList<>();

        String query = "SELECT * FROM showproperty";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("property_id");
                String name = rs.getString("property_name");
                String address = rs.getString("address");
                float size = rs.getFloat("size");
                double rent = rs.getDouble("rent_price");

                String typeName = rs.getString("type_name");
                String desc = rs.getString("description");
                String status = rs.getString("status");

                PropertyDetails pd = new PropertyDetails(id, name, address, size, rent,typeName, desc, status);
                properties.add(pd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static int properties_total_count() {
        String query = "SELECT * from properties_totalcounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean deleteProperty(int propertyId) {
        String sql = "DELETE FROM properties WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, propertyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
