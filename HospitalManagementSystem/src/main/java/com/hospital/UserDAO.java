// UserDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;
    
    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    
    // CREATE User
    public boolean createUser(String userId, String username, String password, 
                             String name, String email, String role, String status) throws SQLException {
        String sql = "INSERT INTO users (user_id, username, password, name, email, role, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, role);
            stmt.setString(7, status);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // READ - Authenticate User
    public UserEntity authenticate(String username, String password, String role) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? " +
                    "AND role = ? AND status = 'Active'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserEntity(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }
    
    // READ - Get All Users
    public List<UserEntity> getAllUsers() throws SQLException {
        List<UserEntity> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(new UserEntity(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                ));
            }
        }
        return users;
    }
    
    // READ - Get User by ID
    public UserEntity getUserById(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserEntity(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }
    
    // UPDATE User
    public boolean updateUser(String userId, String name, String email, 
                             String role, String status) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, role = ?, status = ? " +
                    "WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, role);
            stmt.setString(4, status);
            stmt.setString(5, userId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // DELETE User
    public boolean deleteUser(String userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Check if username exists
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // Get next user ID
    public String getNextUserId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(user_id, 2) AS UNSIGNED)) FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
            return "U" + String.format("%03d", maxId + 1);
        }
    }
}