package model;

public class EndUser extends User {
    private String department;

    public EndUser(String userId, String name, String email,
                   String password, String department) {
        super(userId, name, email, password);
        this.department = department;
    }

    @Override
    public String getRole() { return "End User"; }

    public String getDepartment() { return department; }
}