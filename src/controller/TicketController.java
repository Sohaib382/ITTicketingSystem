package controller;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketController {
    private List<Ticket>      tickets;
    private List<User>        users;
    private List<ITComponent> components;
    private User              loggedInUser;

    public TicketController() {
        tickets    = new ArrayList<>();
        users      = new ArrayList<>();
        components = new ArrayList<>();
        seedData();
    }

    private void seedData() {
        users.add(new EndUser("U001", "Alice Johnson", "alice@org.com", "alice123", "Finance"));
        users.add(new EndUser("U002", "Bob Smith",     "bob@org.com",   "bob123",   "HR"));
        users.add(new ITStaff("S001", "Charlie IT",    "charlie@org.com","charlie123","Networking"));
        users.add(new ITStaff("S002", "Diana Tech",    "diana@org.com", "diana123", "Hardware"));

        components.add(new ITComponent("C001", "Email Server",   "Software"));
        components.add(new ITComponent("C002", "Office Network", "Network"));
        components.add(new ITComponent("C003", "Laptop",         "Hardware"));
        components.add(new ITComponent("C004", "Printer",        "Hardware"));
    }

    public boolean login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                u.getPassword().equals(password)) {
                loggedInUser = u;
                return true;
            }
        }
        return false;
    }

    public void logout() { loggedInUser = null; }

    public Ticket submitTicket(String title, String description,
                               String priority, String componentId) {
        if (!(loggedInUser instanceof EndUser))
            throw new IllegalStateException("Only End Users can submit tickets.");
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Ticket title cannot be empty.");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be empty.");

        ITComponent comp = getComponentById(componentId);
        if (comp == null)
            throw new IllegalArgumentException("Invalid component selected.");

        Ticket t = new Ticket(title, description, priority,
                              (EndUser) loggedInUser, comp);
        tickets.add(t);
        return t;
    }

    public void assignTicket(String ticketId, String staffId) {
        Ticket t  = getTicketById(ticketId);
        ITStaff s = getStaffById(staffId);
        if (t == null) throw new IllegalArgumentException("Ticket not found.");
        if (s == null) throw new IllegalArgumentException("Staff member not found.");
        t.assignTo(s);
    }

    public void updateStatus(String ticketId, String action) {
        Ticket t = getTicketById(ticketId);
        if (t == null) throw new IllegalArgumentException("Ticket not found.");
        switch (action) {
            case "START"    -> t.startProgress();
            case "RESOLVE"  -> t.resolve();
            case "CLOSE"    -> t.close();
            case "ESCALATE" -> t.escalate();
            case "HOLD"     -> t.putOnHold();
            case "REOPEN"   -> t.reOpen();
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }
    }

    public void submitFeedback(String ticketId, int rating, String comments) {
        Ticket t = getTicketById(ticketId);
        if (t == null)
            throw new IllegalArgumentException("Ticket not found.");
        if (t.getStatus() != TicketStatus.RESOLVED &&
            t.getStatus() != TicketStatus.CLOSED)
            throw new IllegalStateException(
                "Feedback only allowed on resolved or closed tickets.");
        t.addFeedback(new Feedback(rating, comments));
    }

    public List<Ticket> getAllTickets()           { return tickets; }

    public List<Ticket> getTicketsByCurrentUser() {
        if (loggedInUser instanceof EndUser eu) {
            return tickets.stream()
                .filter(t -> t.getSubmittedBy().getUserId().equals(eu.getUserId()))
                .collect(Collectors.toList());
        }
        return tickets;
    }

    public List<ITComponent> getComponents()      { return components; }
    public List<User>        getUsers()           { return users; }
    public User              getLoggedInUser()    { return loggedInUser; }

    public List<ITStaff> getAllStaff() {
        return users.stream()
            .filter(u -> u instanceof ITStaff)
            .map(u -> (ITStaff) u)
            .collect(Collectors.toList());
    }

    private Ticket getTicketById(String id) {
        return tickets.stream()
            .filter(t -> t.getTicketId().equals(id))
            .findFirst().orElse(null);
    }

    private ITComponent getComponentById(String id) {
        return components.stream()
            .filter(c -> c.getComponentId().equals(id))
            .findFirst().orElse(null);
    }

    private ITStaff getStaffById(String id) {
        return users.stream()
            .filter(u -> u instanceof ITStaff && u.getUserId().equals(id))
            .map(u -> (ITStaff) u)
            .findFirst().orElse(null);
    }
}