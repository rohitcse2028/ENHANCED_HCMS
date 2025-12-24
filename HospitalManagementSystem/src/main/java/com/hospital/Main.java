import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

 class AppointmentSlotManager {
    // In-memory cache for fast access
    private static final Map<String, Set<String>> bookedSlotsCache = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock(true); // Fair lock
    
    /**
     * Book a slot with thread-safety and database transaction
     */
    public static boolean bookSlot(String doctorId, String date, String time) {
        lock.lock(); // Acquire lock for thread safety
        try {
            String cacheKey = doctorId + "|" + date;
            
            // 1. Check in-memory cache first (fast)
            if (isSlotBookedInCache(cacheKey, time)) {
                System.out.println("[Cache] Slot already booked: " + doctorId + " " + date + " " + time);
                return false;
            }
            
            // 2. Check and book in database (with transaction)
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false); // Start transaction
                
                // 2a. Check if slot is available (with FOR UPDATE lock)
                if (!isSlotAvailableInDB(conn, doctorId, date, time)) {
                    conn.rollback();
                    return false;
                }
                
                // 2b. Book the slot
                if (!bookSlotInDB(conn, doctorId, date, time)) {
                    conn.rollback();
                    return false;
                }
                
                // 2c. Commit transaction
                conn.commit();
                
                // 3. Update cache
                updateCache(cacheKey, time, true);
                
                System.out.println("[Success] Slot booked: Dr." + doctorId + " on " + date + " at " + time);
                return true;
                
            } catch (SQLException e) {
                // Rollback on any error
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) {}
                }
                System.err.println("[Error] Booking failed: " + e.getMessage());
                return false;
            } finally {
                // Cleanup
                if (conn != null) {
                    try { 
                        conn.setAutoCommit(true); 
                        conn.close(); 
                    } catch (SQLException e) {}
                }
            }
        } finally {
            lock.unlock(); // Always release lock
        }
    }
    
    /**
     * Release (cancel) a booked slot
     */
    public static boolean releaseSlot(String doctorId, String date, String time) {
        lock.lock();
        try {
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                
                String sql = "UPDATE appointment_slots SET status = 'AVAILABLE' " +
                            "WHERE doctor_id = ? AND appointment_date = ? " +
                            "AND appointment_time = ? AND status = 'BOOKED'";
                
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, doctorId);
                stmt.setString(2, date);
                stmt.setString(3, time);
                
                int affected = stmt.executeUpdate();
                stmt.close();
                
                if (affected > 0) {
                    // Update cache
                    String cacheKey = doctorId + "|" + date;
                    updateCache(cacheKey, time, false);
                    
                    System.out.println("[Success] Slot released: Dr." + doctorId + " on " + date + " at " + time);
                    return true;
                }
                
                System.out.println("[Warning] Slot not found or already available");
                return false;
                
            } catch (SQLException e) {
                System.err.println("[Error] Releasing slot: " + e.getMessage());
                return false;
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Check if slot is available (thread-safe)
     */
    public static boolean isSlotAvailable(String doctorId, String date, String time) {
        lock.lock();
        try {
            // 1. Check cache first
            String cacheKey = doctorId + "|" + date;
            if (isSlotBookedInCache(cacheKey, time)) {
                return false;
            }
            
            // 2. Check database
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                return isSlotAvailableInDB(conn, doctorId, date, time);
            } catch (SQLException e) {
                System.err.println("[Error] Checking availability: " + e.getMessage());
                return false;
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get all booked slots for a doctor on a specific date
     */
    public static List<String> getBookedSlots(String doctorId, String date) {
        lock.lock();
        try {
            List<String> bookedSlots = new ArrayList<>();
            
            // 1. Check cache
            String cacheKey = doctorId + "|" + date;
            Set<String> cachedSlots = bookedSlotsCache.get(cacheKey);
            if (cachedSlots != null) {
                bookedSlots.addAll(cachedSlots);
            }
            
            // 2. Check database for any additional slots
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                
                String sql = "SELECT appointment_time FROM appointment_slots " +
                            "WHERE doctor_id = ? AND appointment_date = ? " +
                            "AND status = 'BOOKED'";
                
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, doctorId);
                stmt.setString(2, date);
                
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String time = rs.getString("appointment_time");
                    if (!bookedSlots.contains(time)) {
                        bookedSlots.add(time);
                    }
                }
                
                rs.close();
                stmt.close();
                
            } catch (SQLException e) {
                System.err.println("[Error] Getting booked slots: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
            
            Collections.sort(bookedSlots);
            return bookedSlots;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Load all booked slots into cache (call on application startup)
     */
    public static void loadCacheFromDatabase() {
        lock.lock();
        try {
            bookedSlotsCache.clear(); // Clear existing cache
            
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                
                String sql = "SELECT doctor_id, appointment_date, appointment_time " +
                            "FROM appointment_slots WHERE status = 'BOOKED'";
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                int count = 0;
                while (rs.next()) {
                    String doctorId = rs.getString("doctor_id");
                    String date = rs.getString("appointment_date");
                    String time = rs.getString("appointment_time");
                    
                    String cacheKey = doctorId + "|" + date;
                    updateCache(cacheKey, time, true);
                    count++;
                }
                
                rs.close();
                stmt.close();
                
                System.out.println("[Cache] Loaded " + count + " booked slots into cache");
                
            } catch (SQLException e) {
                System.err.println("[Error] Loading cache: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Clear cache (for testing or maintenance)
     */
    public static void clearCache() {
        lock.lock();
        try {
            bookedSlotsCache.clear();
            System.out.println("[Cache] Cache cleared");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get cache statistics
     */
    public static String getCacheStats() {
        lock.lock();
        try {
            int totalSlots = 0;
            for (Set<String> slots : bookedSlotsCache.values()) {
                totalSlots += slots.size();
            }
            
            return String.format(
                "Appointment Slot Cache Stats:\n" +
                "  Cached Dates: %d\n" +
                "  Total Slots: %d\n" +
                "  Lock Status: %s",
                bookedSlotsCache.size(),
                totalSlots,
                lock.isLocked() ? "LOCKED" : "UNLOCKED"
            );
        } finally {
            lock.unlock();
        }
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    /**
     * Check database if slot is available
     */
    private static boolean isSlotAvailableInDB(Connection conn, String doctorId, 
                                               String date, String time) throws SQLException {
        String sql = "SELECT status FROM appointment_slots " +
                    "WHERE doctor_id = ? AND appointment_date = ? " +
                    "AND appointment_time = ? FOR UPDATE"; // Lock row
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, doctorId);
        stmt.setString(2, date);
        stmt.setString(3, time);
        
        ResultSet rs = stmt.executeQuery();
        boolean available = true;
        
        if (rs.next()) {
            if ("BOOKED".equals(rs.getString("status"))) {
                available = false;
            }
        }
        
        rs.close();
        stmt.close();
        return available;
    }
    
    /**
     * Book slot in database
     */
    private static boolean bookSlotInDB(Connection conn, String doctorId, 
                                        String date, String time) throws SQLException {
        String sql = "INSERT INTO appointment_slots " +
                    "(doctor_id, appointment_date, appointment_time, status) " +
                    "VALUES (?, ?, ?, 'BOOKED') " +
                    "ON DUPLICATE KEY UPDATE status = 'BOOKED'";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, doctorId);
        stmt.setString(2, date);
        stmt.setString(3, time);
        
        int affected = stmt.executeUpdate();
        stmt.close();
        
        return affected > 0;
    }
    
    /**
     * Check if slot is booked in cache
     */
    private static boolean isSlotBookedInCache(String cacheKey, String time) {
        Set<String> slots = bookedSlotsCache.get(cacheKey);
        return slots != null && slots.contains(time);
    }
    
    /**
     * Update cache (add or remove slot)
     */
    private static void updateCache(String cacheKey, String time, boolean booked) {
        Set<String> slots = bookedSlotsCache.get(cacheKey);
        if (slots == null) {
            slots = new HashSet<>();
            bookedSlotsCache.put(cacheKey, slots);
        }
        
        if (booked) {
            slots.add(time);
        } else {
            slots.remove(time);
        }
    }
}
