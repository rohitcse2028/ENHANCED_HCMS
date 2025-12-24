// User.java - FIXED VERSION
public abstract class User {
    protected String username;
    
    public User(String username) {
        this.username = username;
    }
    
    public abstract void openDashboard();
    
    // ADD GETTER METHOD
    public String getUsername() {
        return username;
    }
    
    // OPTIONAL: SETTER METHOD
    public void setUsername(String username) {
        this.username = username;
    }
}