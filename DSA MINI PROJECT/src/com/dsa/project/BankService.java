package com.dsa.project;

import java.sql.*;
import java.util.*;

public class BankService {

    private static final Map<Integer, Customer> accountMap = new HashMap<>();
    private static Integer loggedInCustomer = null;
    
    // =============== CHEQUEBOOK QUEUE (DSA: FIFO) ===============
    private static final Queue<ChequebookRequest> chequebookQueue = new LinkedList<>();

    static {
        loadDataFromDB();
    }

    public static void loadDataFromDB() {
        accountMap.clear();
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Customers");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int acc = rs.getInt("AccountNo");
                Customer c = new Customer(
                    acc,
                    rs.getString("Name"),
                    rs.getString("Email"),
                    rs.getString("Phone"),
                    rs.getDouble("Balance")
                );

                try (PreparedStatement ps2 = con.prepareStatement("SELECT Message FROM Transactions WHERE AccountNo = ?")) {
                    ps2.setInt(1, acc);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        c.addTransaction(rs2.getString("Message"));
                    }
                }

                accountMap.put(acc, c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ========== Customer Login ==========
    public static String customerLogin(int accNo, String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Invalid Credentials.";
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT 1 FROM Customer_Login WHERE AccountNo = ? AND Password = ?")) {
            ps.setInt(1, accNo);
            ps.setString(2, password);
            if (ps.executeQuery().next()) {
                loggedInCustomer = accNo;
                loadDataFromDB(); // refresh balance & history
                return "Customer Login Successful.";
            }
            return "Invalid Credentials.";
        } catch (SQLException e) {
            return "Login Error: " + e.getMessage();
        }
    }

    public static Customer getLoggedInCustomer() {
        return loggedInCustomer == null ? null : accountMap.get(loggedInCustomer);
    }

    public static String logout() {
        if (loggedInCustomer == null) return "No customer is logged in.";
        loggedInCustomer = null;
        return "Logout successful.";
    }

    // ========== Account Operations ==========
    
    public static String deposit(double amt) {

        if (loggedInCustomer == null)
            return "❌ Login first.";

        if (amt <= 0)
            return "❌ Invalid amount. Must be greater than zero.";

        Customer c = accountMap.get(loggedInCustomer);
        if (c == null)
            return "❌ Account not found.";

        double newBalance = c.getBalance() + amt;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE Customers SET Balance = ? WHERE AccountNo = ?")) {

            ps.setDouble(1, newBalance);
            ps.setInt(2, loggedInCustomer);
            ps.executeUpdate();

            c.setBalance(newBalance);

            logTransaction("Deposited ₹" + String.format("%.2f", amt));

            return "✅ Deposit Successful. New Balance: ₹" +
                    String.format("%.2f", newBalance);

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error during deposit.";
        }
    }




    public static String withdraw(double amt) {

        if (loggedInCustomer == null)
            return "❌ Login first.";

        if (amt <= 0)
            return "❌ Invalid amount.";

        Customer c = accountMap.get(loggedInCustomer);
        if (c == null)
            return "❌ Account not found.";

        if (amt > c.getBalance())
            return "❌ Insufficient balance.";

        double newBalance = c.getBalance() - amt;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE Customers SET Balance = ? WHERE AccountNo = ?")) {

            ps.setDouble(1, newBalance);
            ps.setInt(2, loggedInCustomer);
            ps.executeUpdate();

            c.setBalance(newBalance);

            logTransaction("Withdrawn ₹" + String.format("%.2f", amt));

            return "✅ Withdrawal Successful. New Balance: ₹" +
                    String.format("%.2f", newBalance);

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error during withdrawal.";
        }
    }



    public static String history() {
        if (loggedInCustomer == null) return "Login first.";
        Customer c = accountMap.get(loggedInCustomer);
        if (c == null) return "Account not found.";

        Stack<String> history = c.getTransactionHistory();
        if (history.isEmpty()) return "No transaction history.";

        StringBuilder sb = new StringBuilder("Transaction History:\n");
        List<String> list = new ArrayList<>(history);
        Collections.reverse(list);
        for (String msg : list) {
            sb.append("- ").append(msg).append("\n");
        }
        return sb.toString();
    }
    
    public static String checkBalance() {
        if (loggedInCustomer == null) return "Login first.";
        Customer c = accountMap.get(loggedInCustomer);
        if (c == null) return "Account not found.";

        return "✅ Your current balance is: ₹" + String.format("%.2f", c.getBalance());
    }

    // ========== Chequebook Request (DSA: Queue) ==========
    public static String requestChequebook() {
        if (loggedInCustomer == null) return "❌ Login first.";
        Customer c = accountMap.get(loggedInCustomer);
        if (c == null) return "❌ Account not found.";

        // Check if already has a pending request
        for (ChequebookRequest req : chequebookQueue) {
            if (req.getAccountNo() == loggedInCustomer && !req.isProcessed()) {
                return "✅ Chequebook request already pending.";
            }
        }

        chequebookQueue.offer(new ChequebookRequest(loggedInCustomer, c.getName()));
        return "✅ Chequebook request submitted! Awaiting manager approval.";
    }

    public static List<ChequebookRequest> getPendingChequebookRequests() {
        List<ChequebookRequest> pending = new ArrayList<>();
        for (ChequebookRequest req : chequebookQueue) {
            if (!req.isProcessed()) {
                pending.add(req);
            }
        }
        return pending;
    }

    public static String processNextChequebook() {
        if (chequebookQueue.isEmpty()) {
            return "📭 No pending chequebook requests.";
        }

        // Find the first unprocessed request (FIFO)
        ChequebookRequest next = null;
        for (ChequebookRequest req : chequebookQueue) {
            if (!req.isProcessed()) {
                next = req;
                break;
            }
        }

        if (next == null) {
            return "📭 No pending chequebook requests.";
        }

        next.markProcessed();
        logTransaction(next.getAccountNo(), "Chequebook issued (50 leaves)");

        loadDataFromDB();

        return "✅ Chequebook processed for Account: " + next.getAccountNo() + " (" + next.getCustomerName() + ")";
    }

    // ========== Helper: Log Transaction ==========
    private static void logTransaction(String message) {
        if (loggedInCustomer == null) return;

        Customer c = accountMap.get(loggedInCustomer);
        if (c != null) {
            c.addTransaction(message);
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO Transactions(AccountNo, Message) VALUES (?, ?)")) {
            ps.setInt(1, loggedInCustomer);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }


    private static void logTransaction(Integer accountNo, String message) {
        if (accountNo == null) return;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO Transactions(AccountNo, Message) VALUES (?, ?)")) {
            ps.setInt(1, accountNo);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            // Silent fail for academic project
        }
    }

    // ========== Manager ==========
    public static boolean validateManager(String username, String password) {
        if (username == null || password == null) return false;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT 1 FROM Manager_Login WHERE Username = ? AND Password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(accountMap.values());
    }

    // ========== Account Creation ==========
    public static String createAccount(String name, String email, String phone, double deposit, String password) {
        if (name == null || name.trim().isEmpty()) return "Name is required.";
        if (phone == null || !phone.matches("\\d{10}")) return "Invalid Phone (10 digits required).";
        if (deposit < 0) return "Deposit amount cannot be negative.";
        if (password == null || password.length() < 4) return "Password must be at least 4 characters.";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Customers(Name, Email, Phone, Balance) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name.trim());
                ps.setString(2, email == null ? "" : email.trim());
                ps.setString(3, phone);
                ps.setDouble(4, deposit);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int accNo = rs.getInt(1);

                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO Customer_Login(AccountNo, Password) VALUES (?, ?)"
                );
                ps2.setInt(1, accNo);
                ps2.setString(2, password);
                ps2.executeUpdate();

                con.commit();

                Customer c = new Customer(accNo, name.trim(), email, phone, deposit);
                accountMap.put(accNo, c);
                logTransaction(accNo, "Account Created with ₹" + String.format("%.2f", deposit));

                return "✅ Account Created Successfully!\nAccount Number: " + accNo;
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
                return "Database Error: " + e.getMessage();
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Connection Error.";
        }
    }

    // =============== INNER CLASS: ChequebookRequest ===============
    public static class ChequebookRequest {
        private final int accountNo;
        private final String customerName;
        private boolean processed;

        public ChequebookRequest(int accountNo, String customerName) {
            this.accountNo = accountNo;
            this.customerName = customerName;
            this.processed = false;
        }

        public int getAccountNo() { return accountNo; }
        public String getCustomerName() { return customerName; }
        public boolean isProcessed() { return processed; }
        public void markProcessed() { this.processed = true; }

        @Override
        public String toString() {
            return "Account: " + accountNo + " | Name: " + customerName;
        }
    }
}