// AppointmentDAO.java - NEW FILE
 

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private Connection connection;
    
    public AppointmentDAO(Connection connection) {
        this.connection = connection;
    }
    
    // CREATE
    public boolean createAppointment(String appointmentId, String patientId, 
                                    String doctorId, Date date, Time time, 
                                    String type, String status) throws SQLException {
        String sql = "INSERT INTO appointments " +
                    "(appointment_id, patient_id, doctor_id, " +
                    "appointment_date, appointment_time, type, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            stmt.setString(2, patientId);
            stmt.setString(3, doctorId);
            stmt.setDate(4, date);
            stmt.setTime(5, time);
            stmt.setString(6, type);
            stmt.setString(7, status);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // READ - All appointments
    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getString("appointment_id"),
                    rs.getString("patient_id"),
                    rs.getString("doctor_id"),
                    rs.getDate("appointment_date"),
                    rs.getTime("appointment_time"),
                    rs.getString("type"),
                    rs.getString("status")
                ));
            }
        }
        return appointments;
    }
    
    // READ - By Patient ID
    public List<Appointment> getAppointmentsByPatient(String patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                        rs.getString("appointment_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("type"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return appointments;
    }
    
    // READ - By Doctor ID
    public List<Appointment> getAppointmentsByDoctor(String doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                        rs.getString("appointment_id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getDate("appointment_date"),
                        rs.getTime("appointment_time"),
                        rs.getString("type"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return appointments;
    }
    
    // UPDATE
    public boolean updateAppointmentStatus(String appointmentId, String newStatus) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, appointmentId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // DELETE
    public boolean deleteAppointment(String appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // Check slot availability
    public boolean isSlotAvailable(String doctorId, Date date, Time time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE " +
                    "doctor_id = ? AND appointment_date = ? AND appointment_time = ? " +
                    "AND status != 'Cancelled'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            stmt.setDate(2, date);
            stmt.setTime(3, time);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }
    
    // Get next appointment ID
    public String getNextAppointmentId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(appointment_id, 2) AS UNSIGNED)) FROM appointments";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
            return "A" + String.format("%03d", maxId + 1);
        }
    }
}

// Appointment Entity Class
class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String type;
    private String status;
    
    public Appointment(String appointmentId, String patientId, String doctorId,
                      Date appointmentDate, Time appointmentTime, 
                      String type, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.type = type;
        this.status = status;
    }
    
    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }
    
    public Time getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Time appointmentTime) { this.appointmentTime = appointmentTime; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Appointment{" +
               "appointmentId='" + appointmentId + '\'' +
               ", patientId='" + patientId + '\'' +
               ", doctorId='" + doctorId + '\'' +
               ", appointmentDate=" + appointmentDate +
               ", appointmentTime=" + appointmentTime +
               ", type='" + type + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
