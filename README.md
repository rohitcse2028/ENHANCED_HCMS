🏥 Hospital Management System
Java GUI + JDBC + DAO + Multithreading + Servlets + JSP Project
This is a complete Hospital Management System built using
Java (Swing GUI) + JDBC + MySQL + DAO Pattern + Multithreading + Servlets + JSP.

It fulfills Review-1 and Review-2 academic requirements completely.

🚀 Features
🎯 Core Modules
1.	User Login (Admin / Doctor / Patient)
2.	User Registration
3.	Lost & Found Item Management
4.	Dashboard with user info
5.	Search & Filter system
6.	CRUD operations using DAO
7.	Email, Phone, Name Validation
8.	User Factory Pattern
9.	Dark / Light Theme Support

⚙️ Technical Highlights
 1. Object-Oriented Concepts-
•	Inheritance (Admin, Doctor, Patient → User)
•	Polymorphism (openDashboard())
•	Abstraction (User class)
•	Interfaces (DAO-like structure)
•	Factory Pattern (UserFactory)

2.  Multithreading
•	Background UI thread: LostAndFoundFrameThread
•	Thread Safety: ReentrantLock
•	Synchronization for shared resources

3.  Collections & Generics
•	List<Item>
•	Generic DAO structure

 4. Database + JDBC
•	MySQL database
•	HikariCP Connection Pooling
•	Prepared Statements (secure)
•	CRUD operations (UserDAO)


 5. Servlets + JSP (Review-2)
	LoginServlet
	RegisterServlet
	ItemServlet	
	JSP pages: login.jsp, register.jsp, dashboard.jsp, items.jsp
	web.xml servlet mappings

6.  Extra Efforts
•	PDF generation (iText)
•	Charts (JFreeChart)
•	Theme Toggle (Dark / Light Mode)

📁 Project Structure
HospitalManagementSystem/
│
├── README.md
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hospital/
│       │        ├── *.java (26 source classes)
│       │        ├── LoginServlet.java
│       │        ├── RegisterServlet.java
│       │        └── ItemServlet.java
│       │
│       └── webapp/
│           ├── login.jsp
│           ├── register.jsp
│           ├── dashboard.jsp
│           ├── items.jsp
│           │
│           └── WEB-INF/
│                └── web.xml


 Important Java Classes
	User.java
	Abstract base class

Common attributes & methods
🔵 Admin.java, Doctor.java, Patient.java
Inherit User
Polymorphic behaviour

🔵 UserFactory.java
Returns correct user object based on role

🔵 LostAndFoundManager.java
Stores & manages items
Thread-safe using ReentrantLock
Add, Search, GetAll, Batch Add

🔵 LostAndFoundFrameThread.java
Separate GUI update thread

🔵 BaseDAO.java, UserDAO.java

Database operations
executeQuery(), executeUpdate(), CRUD

🔋 DatabaseConnection.java
HikariCP Connection pool

🔵 InputValidator.java

Validates name, email, phone

📦 Item.java

POJO class for lost/found items

🌐 Servlets (Review-2)
✔ LoginServlet.java

Handles:
Username + Password check

Session creation
✔ RegisterServlet.java

Handles:
New user registration
Validations

Redirect to login

✔ ItemServlet.java

Handles:
Add new item
Show all items to JSP

Mapped in web.xml:
/login
/register
/items

🖥️ JSP Pages
✔ login.jsp
Simple Login UI

✔ register.jsp
User Registration UI

✔ dashboard.jsp
Shows logged-in username

✔ items.jsp
Displays table of all lost/found items + form to add more

📦 How to Run (If using Tomcat)
Place folder in:
tomcat/webapps/HospitalManagementSystem

Start Tomcat
Visit:
http://localhost:8080/HospitalManagementSystem/login.jsp
Jar files required:
── mysql-connector.jar
── HikariCP.jar
── itextpdf.jar
── jfreechart.jar

 Review-1 Achievement
Topic	Status
Inheritance	✔
Polymorphism	✔
Abstract Classes	✔
Collections	✔
Generics	✔
Multithreading	✔
Synchronization	✔
DAO Pattern	✔
JDBC	✔


 Review-2 Achievement
Requirement	Status
Servlet Implementation	✔
Code Quality	✔
Extra Effort (PDF, Charts, Theme) ✔

👤 Developer
ROHIT SHUKLA

🎉 Conclusion
This project combines:
Java OOP
Multithreading
Database handling
GUI
Web components (Servlet + JSP)
Industry patterns (DAO, Factory, Pooling)
