# IT Ticketing System

A Java Swing desktop application for managing IT support tickets.

---

## Group Members

- Abbas Ali (24P-0649)  
- Muhammad Shoaib Shahid (24P-0571)  
- Muhammad Sohaib Bukhari (24P-0732)  

---

## Tech Stack

- Java 17  
- Java Swing (GUI)  
- No external libraries required  

---

## Default Login Credentials

| Role      | Email              | Password    |
|-----------|------------------|------------|
| End User  | alice@org.com     | alice123   |
| End User  | bob@org.com       | bob123     |
| IT Staff  | charlie@org.com   | charlie123 |
| IT Staff  | diana@org.com     | diana123   |


---

## How to Run the Project

### 1. Prerequisites

Make sure these tools are installed on your machine:

- Java JDK 17 or higher  

You can verify installation with:

bash
java -version
javac -version

## 2.Clone the Repository
git clone https://github.com/Sohaib382/ITTicketingSystem.git
cd ITTicketingSystem
# 3.Compile the Project
javac -d out -sourcepath src src/Main.java
## 4.Run the Application
java -cp out Main

This will:

## 5.Compile the project
Launch the Java Swing application
 First-Time Notes
Make sure the out/ folder exists before compiling:
mkdir out
No internet connection or Maven required
Use the default credentials listed above to log in
## Features Implemented
Login / Logout (End User and IT Staff roles)
End User: Submit IT tickets with priority and component selection
End User: View ticket status and notification history
End User: Submit feedback on resolved tickets
IT Staff: View all tickets with colour-coded status
IT Staff: Assign tickets to available staff members
IT Staff: Update ticket status:
Start
Resolve
Close
Escalate
Hold
Re-open
