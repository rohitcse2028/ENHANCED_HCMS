// src/main/java/com/healthcare/util/AuditLogger.java
 

import java.sql.*;
import java.time.LocalDateTime;

public class AuditLogger {
    private static final String INSERT_AUDIT_SQL = 
        "INSERT INTO audit_logs (user_id, action, entity_type, entity_id, " +
        "timestamp, details) VALUES (?, ?, ?, ?, ?, ?)";
    
    public static void log(String action, String details) {
        log(null, action, null, null, details);
    }
    
    public static void log(String userId, String action, String entityType, 
                          String entityId, String details) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_AUDIT_SQL)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, entityType);
            stmt.setString(4, entityId);
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(6, details);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Fallback to file logging if DB fails
            logToFile(action + ": " + details);
        }
    }
    
    private static void logToFile(String message) {
        // Simple file logging as fallback
        System.err.println("[AUDIT] " + message);
    }
}
