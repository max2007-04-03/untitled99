package ua.opnu.service;

import ua.opnu.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private Connection connection;

    public ClientService(Connection connection) {
        this.connection = connection;
    }

    public long create(String name) throws SQLException {
        validateName(name);
        String sql = "INSERT INTO client (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new SQLException("Error retrieving the ID of the created client.");
    }

    public String getById(long id) throws SQLException {
        String sql = "SELECT name FROM client WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("name");
            }
        }
        throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
    }

    public void setName(long id, String name) throws SQLException {
        validateName(name);
        String sql = "UPDATE client SET name = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setLong(2, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Client with ID " + id + " not found for update.");
            }
        }
    }

    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Client with ID " + id + " not found for deletion.");
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name FROM client";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(rs.getLong("id"), rs.getString("name")));
            }
        }
        return clients;
    }

    private void validateName(String name) {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 1000) {
            throw new IllegalArgumentException("Client name must be between 2 and 1000 characters.");
        }
    }
}