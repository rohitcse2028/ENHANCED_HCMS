// TransactionManager.java - NEW FILE
 

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    
    public interface TransactionTask {
        void execute(Connection conn) throws SQLException;
    }
    
    public static boolean executeTransaction(TransactionTask task) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaction start
            
            task.execute(conn); // Execute all SQL operations
            
            conn.commit(); // Sab kuch sahi gaya to commit
            success = true;
            System.out.println("✓ Transaction successful");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Koi error aaya to sab rollback
                    System.out.println("✗ Transaction rolled back: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset to default
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
    
    // Batch transaction for multiple operations
    public static boolean executeBatchTransaction(TransactionTask... tasks) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            for (TransactionTask task : tasks) {
                task.execute(conn);
            }
            
            conn.commit();
            success = true;
            System.out.println("✓ Batch transaction successful");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("✗ Batch transaction rolled back");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}
