package model;

public class ITStaff extends User {
    private String specialization;
    private boolean isAvailable;

    public ITStaff(String userId, String name, String email,
                   String password, String specialization) {
        super(userId, name, email, password);
        this.specialization = specialization;
        this.isAvailable = true;
    }

    @Override
    public String getRole() { return "IT Staff"; }

    public String getSpecialization() { return specialization; }
    public boolean isAvailable()      { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}