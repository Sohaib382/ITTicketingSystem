package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private static int counter = 1000;

    private String ticketId;
    private String title;
    private String description;
    private String priority;
    private TicketStatus status;
    private LocalDateTime createdAt;

    private EndUser submittedBy;
    private ITStaff assignedTo;
    private ITComponent affectedComponent;
    private List<Notification> notifications;
    private Feedback feedback;

    public Ticket(String title, String description, String priority,
                  EndUser submittedBy, ITComponent affectedComponent) {
        this.ticketId          = "TKT-" + (++counter);
        this.title             = title;
        this.description       = description;
        this.priority          = priority;
        this.status            = TicketStatus.NEW;
        this.createdAt         = LocalDateTime.now();
        this.submittedBy       = submittedBy;
        this.affectedComponent = affectedComponent;
        this.notifications     = new ArrayList<>();
        addNotification("Ticket created by " + submittedBy.getName());
    }

    public void assignTo(ITStaff staff) {
        this.assignedTo = staff;
        this.status     = TicketStatus.PENDING;
        addNotification("Ticket assigned to " + staff.getName());
    }

    public void startProgress() {
        if (status == TicketStatus.PENDING) {
            status = TicketStatus.IN_PROGRESS;
            addNotification("Work started on ticket.");
        }
    }

    public void resolve() {
        if (status == TicketStatus.IN_PROGRESS || status == TicketStatus.ESCALATED) {
            status = TicketStatus.RESOLVED;
            addNotification("Ticket resolved.");
        }
    }

    public void close() {
        if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.CLOSED;
            addNotification("Ticket closed.");
        }
    }

    public void escalate() {
        status = TicketStatus.ESCALATED;
        addNotification("Ticket escalated.");
    }

    public void putOnHold() {
        status = TicketStatus.ON_HOLD;
        addNotification("Ticket put on hold.");
    }

    public void reOpen() {
        if (status == TicketStatus.RESOLVED) {
            status = TicketStatus.IN_PROGRESS;
            addNotification("Ticket re-opened.");
        }
    }

    public void addFeedback(Feedback fb) {
        this.feedback = fb;
        addNotification("Feedback submitted: " + fb.getRating() + "/5");
    }

    private void addNotification(String message) {
        notifications.add(new Notification(message));
    }

    public String getTicketId()              { return ticketId; }
    public String getTitle()                 { return title; }
    public String getDescription()           { return description; }
    public String getPriority()              { return priority; }
    public TicketStatus getStatus()          { return status; }
    public EndUser getSubmittedBy()          { return submittedBy; }
    public ITStaff getAssignedTo()           { return assignedTo; }
    public ITComponent getAffectedComponent(){ return affectedComponent; }
    public List<Notification> getNotifications() { return notifications; }
    public Feedback getFeedback()            { return feedback; }

    public String getCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return ticketId + " — " + title + " [" + status + "]";
    }
}