// RunSystem.java
public class RunSystem {
    public static void main(String[] args) {
        System.out.println("Healthcare System Starting...");
        
        try {
            // Pehle saare important classes compile ho jayengi
            new LoginFrame().setVisible(true);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}