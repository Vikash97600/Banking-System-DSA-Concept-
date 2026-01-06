**Project Overview & Technical Details**
The system, titled "Union Bank of India - Banking System," is a desktop application designed to automate core retail banking operations.

Architecture: Follows a Client-Server model where the Java GUI (Swing) interacts with a MySQL backend via the JDBC driver (mysql-connector-j-9.5.0.jar).

**Key Modules:**

- **Customer Module:** Handles account registration, secure login, real-time balance inquiry, deposits, and withdrawals.

- **Manager Module:** Provides administrative oversight, including viewing all customer records and processing service requests like chequebook issuances.

- **Transaction Engine:** Maintains a detailed history of all financial activities (account creation, deposits, withdrawals) for auditing.

- **Security:** Implements role-based access control (RBAC) and password-protected authentication for both users and administrators.



# Union Bank of India - Banking Management System

A robust, desktop-based Banking Management System developed in Java. This application simulates a real-world banking environment, allowing for secure customer transactions and administrative management.

## 📺 Demo
**Project video :**  https://github.com/user-attachments/assets/5437ea5d-3c53-4765-b08e-eafc667bee87

*Features shown: Account creation, deposit/withdrawal, and manager-level approvals.*

## 🌟 Key Features
### For Customers
- **Instant Account Creation:** Register with personal details and a secure password.
- **Financial Operations:** Real-time deposits and withdrawals with automated balance updates.
- **Service Requests:** Request chequebooks directly from the dashboard.
- **Transaction History:** View a chronological log of all account activities.
- **Balance Inquiry:** Check current funds at any time.

### For Managers
- **Manager Login:** Secure portal for administrative tasks.
- **Customer Overview:** View a complete list of all registered accounts and their current balances.
- **Request Processing:** Review and approve pending chequebook requests (FIFO basis).

## 🛠️ Technical Stack
- **Frontend:** Java Swing (AWT for event handling)
- **Backend:** MySQL Database
- **Database Connectivity:** JDBC (MySQL Connector/J 9.5.0)
- **IDE:** Eclipse

## 📂 Project Structure
- `.project` & `.classpath`: Eclipse environment configurations.
- `src/`: Contains all Java source files (GUI, Database Connection, Logic).
- `bin/`: Compiled bytecode.

## 🚀 Getting Started
### Prerequisites
- JDK 11 or higher.
- MySQL Server installed and running.
- MySQL Connector JAR (included in the build path).

### Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Vikash97600/Banking-System-DSA-Concept-

2. **Setup Database:**
- Create a database named banking_db.
- Run the provided SQL scripts (if available) to create the users, accounts, and transactions tables.

3. **Configure Connection:**
- Update the database credentials (URL, Username, Password) in your DatabaseConnection.java file.

4. **Run:**
- Import the project into Eclipse and run the main entry point class.

🛡️ Security
The system ensures data integrity through:
- Role-based authentication (Manager vs. Customer).
- Input validation for phone numbers and email formats.
- Atomic transaction processing to prevent data corruption.
