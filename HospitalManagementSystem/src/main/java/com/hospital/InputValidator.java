// src/main/java/com/healthcare/util/InputValidator.java
 

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    
    public static List<String> validateLogin(String username, String password) {
        List<String> errors = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            errors.add("Username is required");
        } else if (username.length() < 3 || username.length() > 50) {
            errors.add("Username must be 3-50 characters");
        }
        
        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
        } else if (password.length() < 8) {
            errors.add("Password must be at least 8 characters");
        }
        
        return errors;
    }
    
    public static List<String> validateUser(String name, String email, String phone) {
        List<String> errors = new ArrayList<>();
        
        if (!NAME_PATTERN.matcher(name).matches()) {
            errors.add("Name must contain only letters (2-50 characters)");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
        
        if (phone != null && !phone.trim().isEmpty() && 
            !PHONE_PATTERN.matcher(phone).matches()) {
            errors.add("Phone must be 10 digits");
        }
        
        return errors;
    }
}
