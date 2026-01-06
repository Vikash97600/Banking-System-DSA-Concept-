package com.dsa.project;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

public class BankUI {

    private static JFrame frame;
    private static JTextField tAcc, tName, tEmail, tPhone, tAmt;
    private static JTextArea output;

    private static JButton createBtn, loginBtn, logoutBtn;
    private static JButton depositBtn, withdrawBtn, historyBtn, checkBalanceBtn; // ✅ Check Balance
    private static JButton managerLoginBtn, viewCustomersBtn;
    private static JButton requestChequeBtn, processChequeBtn;

    private static final Color PRIMARY_BLUE = new Color(30, 58, 138);    // Deep blue
    private static final Color ACCENT_GOLD = new Color(234, 179, 8);     // Gold
    private static final Color LIGHT_BLUE = new Color(219, 234, 254);
    private static final Color SUCCESS_GREEN = new Color(16, 185, 129);
    private static final Color ERROR_RED = new Color(239, 68, 68);
    private static final Color BACKGROUND = new Color(249, 250, 251);
    private static final Color PANEL_BG = new Color(255, 255, 255);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("🏦 Union Bank Of India - Banking System");
        frame.setSize(850, 770); 
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(BACKGROUND);

        JLabel title = new JLabel("Union Bank Of India", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(PRIMARY_BLUE);
        title.setBounds(0, 10, 850, 50);
        frame.add(title);

        JLabel subtitle = new JLabel("Secure • Fast • Trusted", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        subtitle.setBounds(0, 55, 850, 25);
        frame.add(subtitle);

        JPanel inputPanel = new JPanel(null);
        inputPanel.setBackground(PANEL_BG);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
            "Account Details",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            PRIMARY_BLUE
        ));
        inputPanel.setBounds(40, 90, 450, 230);
        frame.add(inputPanel);

        tAcc = createField(inputPanel, "Account No", 30, 30, 380);
        tAcc.setEditable(false);
        tAcc.setBackground(LIGHT_BLUE);

        tName = createField(inputPanel, "Full Name", 30, 70, 380);
        tEmail = createField(inputPanel, "Email", 30, 110, 380);
        tPhone = createField(inputPanel, "Phone (10 digits)", 30, 150, 380);
        tAmt = createField(inputPanel, "Amount (₹)", 30, 190, 380);

        JPanel actionPanel = new JPanel(null);
        actionPanel.setBackground(PANEL_BG);
        actionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_GOLD, 1),
            "Actions",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            ACCENT_GOLD
        ));
        actionPanel.setBounds(520, 90, 280, 320); 
        frame.add(actionPanel);

        createBtn = createStyledButton("Create Account", 20, 20, PRIMARY_BLUE);
        actionPanel.add(createBtn);

        loginBtn = createStyledButton("Customer Login", 150, 20, new Color(79, 70, 229));
        actionPanel.add(loginBtn);

        managerLoginBtn = createStyledButton("Manager Login", 85, 70, new Color(124, 45, 182));
        actionPanel.add(managerLoginBtn);

        depositBtn = createStyledButton("Deposit", 20, 20, SUCCESS_GREEN);
        actionPanel.add(depositBtn);

        withdrawBtn = createStyledButton("Withdraw", 150, 20, ERROR_RED);
        actionPanel.add(withdrawBtn);

        historyBtn = createStyledButton("View History", 20, 70, new Color(14, 165, 233));
        actionPanel.add(historyBtn);

        checkBalanceBtn = createStyledButton("Check Balance", 150, 70, new Color(15, 118, 105)); 
        actionPanel.add(checkBalanceBtn);

        requestChequeBtn = createStyledButton("Req. Chequebook", 20, 120, new Color(9, 103, 99)); 
        actionPanel.add(requestChequeBtn);

        logoutBtn = createStyledButton("Logout", 150, 120, new Color(168, 85, 247));
        actionPanel.add(logoutBtn);

       
        viewCustomersBtn = createStyledButton("View Customers", 20, 20, PRIMARY_BLUE);
        actionPanel.add(viewCustomersBtn);

        processChequeBtn = createStyledButton("Process Cheques", 20, 70, new Color(234, 88, 12)); 
        actionPanel.add(processChequeBtn);

  
        depositBtn.setVisible(false);
        withdrawBtn.setVisible(false);
        historyBtn.setVisible(false);
        checkBalanceBtn.setVisible(false);
        requestChequeBtn.setVisible(false);
        logoutBtn.setVisible(false);
        viewCustomersBtn.setVisible(false);
        processChequeBtn.setVisible(false);

      
        JLabel outputLabel = new JLabel("System Messages & Output:");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 13));
        outputLabel.setBounds(40, 430, 200, 25);
        frame.add(outputLabel);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setBackground(new Color(248, 250, 252));
        output.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBounds(40, 460, 760, 260);
        frame.add(scrollPane);

       
        setupActionListeners();

        setLoggedOutUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JTextField createField(JPanel panel, String label, int x, int y, int width) {
        JLabel l = new JLabel(label + ":");
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setBounds(x, y, 140, 25);
        panel.add(l);

        JTextField t = new JTextField();
        t.setBounds(x + 140, y, width - 140, 25);
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        panel.add(t);
        return t;
    }

    private static JButton createStyledButton(String text, int x, int y, Color bg) {
        JButton b = new JButton(text);
        b.setBounds(x, y, 120, 35);
        b.setBackground(bg);
        b.setForeground(Color.BLACK); 
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 11)); 
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(bg.darker());
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });

        return b;
    }

    private static void setupActionListeners() {
        createBtn.addActionListener(e -> handleCreateAccount());
        loginBtn.addActionListener(e -> handleCustomerLogin());
        logoutBtn.addActionListener(e -> handleLogout());
        depositBtn.addActionListener(e -> handleDeposit());
        withdrawBtn.addActionListener(e -> handleWithdraw());
        historyBtn.addActionListener(e -> handleHistory());
        checkBalanceBtn.addActionListener(e -> handleCheckBalance());
        managerLoginBtn.addActionListener(e -> handleManagerLogin());
        viewCustomersBtn.addActionListener(e -> handleViewCustomers());
        requestChequeBtn.addActionListener(e -> handleRequestChequebook());
        processChequeBtn.addActionListener(e -> handleProcessChequebook());
    }

    private static void handleCreateAccount() {
        if (isEmpty(tName, tPhone, tAmt)) {
            output.setText("⚠️ Please fill Name, Email, Phone, and Amount fields. All fields are mandatory.");
            output.setForeground(ERROR_RED);
            return;
        }

        try {
            double deposit = Double.parseDouble(tAmt.getText());
            JPasswordField p1 = new JPasswordField();
            JPasswordField p2 = new JPasswordField();
            Object[] msg = {"Set Password (min 4 chars):", p1, "Confirm Password:", p2};

            int opt = JOptionPane.showConfirmDialog(frame, msg, "🔒 Set Account Password", JOptionPane.OK_CANCEL_OPTION);
            if (opt != JOptionPane.OK_OPTION) return;

            String pass1 = new String(p1.getPassword());
            String pass2 = new String(p2.getPassword());
            if (!pass1.equals(pass2)) {
                output.setText("❌ Passwords do not match.");
                output.setForeground(ERROR_RED);
                return;
            }

            String result = BankService.createAccount(
                tName.getText(), tEmail.getText(), tPhone.getText(), deposit, pass1
            );
            if (result.startsWith("✅")) {
                output.setForeground(SUCCESS_GREEN);
                clearFields();
            } else {
                output.setForeground(ERROR_RED);
            }
            output.setText(result);
        } catch (NumberFormatException ex) {
            output.setText("❌ Invalid amount. Please enter a valid number.");
            output.setForeground(ERROR_RED);
        }
    }

    private static void handleCustomerLogin() {
        JTextField accField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] msg = {"Account Number:", accField, "Password:", passField};

        int opt = JOptionPane.showConfirmDialog(frame, msg, "🔐 Customer Login", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) return;

        try {
            int accNo = Integer.parseInt(accField.getText());
            String result = BankService.customerLogin(accNo, new String(passField.getPassword()));
            if (result.contains("Successful")) {
                output.setForeground(SUCCESS_GREEN);
                Customer c = BankService.getLoggedInCustomer();
                tAcc.setText(String.valueOf(c.getAccountNo()));
                tName.setText(c.getName());
                tEmail.setText(c.getEmail());
                tPhone.setText(c.getPhone());
                tAmt.setText(new DecimalFormat("#.##").format(c.getBalance()));
                setCustomerLoggedInUI();
            } else {
                output.setForeground(ERROR_RED);
            }
            output.setText(result);
        } catch (NumberFormatException ex) {
            output.setText("❌ Invalid account number.");
            output.setForeground(ERROR_RED);
        }
    }

    private static void handleLogout() {
        String result = BankService.logout();
        output.setText(result);
        output.setForeground(new Color(107, 114, 128)); // gray
        clearFields();
        setLoggedOutUI();
    }

    private static void handleDeposit() {
        try {
            double amt = Double.parseDouble(tAmt.getText());
            String result = BankService.deposit(amt);
            if (result.contains("Successful")) {
                output.setForeground(SUCCESS_GREEN);
                Customer c = BankService.getLoggedInCustomer();
                if (c != null) tAmt.setText(new DecimalFormat("#.##").format(c.getBalance()));
            } else {
                output.setForeground(ERROR_RED);
            }
            output.setText(result);
        } catch (NumberFormatException ex) {
            output.setText("❌ Please enter a valid amount to deposit.");
            output.setForeground(ERROR_RED);
        }
    }

    private static void handleWithdraw() {
        try {
            double amt = Double.parseDouble(tAmt.getText());
            String result = BankService.withdraw(amt);
            if (result.contains("Successful")) {
                output.setForeground(SUCCESS_GREEN);
                Customer c = BankService.getLoggedInCustomer();
                if (c != null) tAmt.setText(new DecimalFormat("#.##").format(c.getBalance()));
            } else {
                output.setForeground(ERROR_RED);
            }
            output.setText(result);
        } catch (NumberFormatException ex) {
            output.setText("❌ Please enter a valid amount to withdraw.");
            output.setForeground(ERROR_RED);
        }
    }

    private static void handleHistory() {
        String result = BankService.history();
        if (result.contains("Login first") || result.contains("not found")) {
            output.setForeground(ERROR_RED);
        } else {
            output.setForeground(new Color(15, 77, 138)); 
        }
        output.setText(result);
    }

    private static void handleCheckBalance() {
        String result = BankService.checkBalance();
        if (result.contains("✅")) {
            output.setForeground(SUCCESS_GREEN);
            Customer c = BankService.getLoggedInCustomer();
            if (c != null) {
                tAmt.setText(new DecimalFormat("#.##").format(c.getBalance()));
            }
        } else {
            output.setForeground(ERROR_RED);
        }
        output.setText(result);
    }

    private static void handleRequestChequebook() {
        String result = BankService.requestChequebook();
        if (result.contains("✅")) {
            output.setForeground(SUCCESS_GREEN);
        } else {
            output.setForeground(ERROR_RED);
        }
        output.setText(result);
    }

    private static void handleProcessChequebook() {
        List<BankService.ChequebookRequest> pending = BankService.getPendingChequebookRequests();
        if (pending.isEmpty()) {
            output.setText("📭 No pending chequebook requests.");
            output.setForeground(new Color(107, 114, 128));
            return;
        }

        StringBuilder sb = new StringBuilder("Pending Chequebook Requests (FIFO):\n");
        sb.append("-------------------------------\n");
        for (int i = 0; i < pending.size(); i++) {
            sb.append((i+1)).append(". ").append(pending.get(i)).append("\n");
        }
        sb.append("\nProcess the *next* (first) request?");

        int choice = JOptionPane.showConfirmDialog(
            frame,
            new JLabel("<html><pre>" + sb.toString().replace("\n", "<br>") + "</pre></html>"),
            "📬 Pending Chequebook Requests",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            String result = BankService.processNextChequebook();
            output.setText(result);
            output.setForeground(result.contains("✅") ? SUCCESS_GREEN : ERROR_RED);
        }
    }

    private static void handleManagerLogin() {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        Object[] msg = {"Username:", user, "Password:", pass};

        int opt = JOptionPane.showConfirmDialog(frame, msg, "👨‍💼 Manager Login", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            if (BankService.validateManager(user.getText(), new String(pass.getPassword()))) {
                output.setText("✅ Manager Login Successful.");
                output.setForeground(SUCCESS_GREEN);
                setManagerUI();
            } else {
                output.setText("❌ Invalid manager credentials.");
                output.setForeground(ERROR_RED);
            }
        }
    }

    private static void handleViewCustomers() {
        List<Customer> customers = BankService.getAllCustomers();
        if (customers.isEmpty()) {
            output.setText("📭 No customers found.");
            output.setForeground(new Color(107, 114, 128));
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-35s %-15s%n", "Account No", "Name", "Balance"));
        sb.append("---------------------------------------------------------------------\n");
        for (Customer c : customers) {
            sb.append(String.format("%-15d %-35s ₹%-15f%n", c.getAccountNo(), c.getName(), c.getBalance()));
        }
        output.setText(sb.toString());
        output.setForeground(new Color(15, 77, 138));
    }


    private static void setCustomerLoggedInUI() {
        createBtn.setVisible(false);
        loginBtn.setVisible(false);
        managerLoginBtn.setVisible(false);

        depositBtn.setVisible(true);
        withdrawBtn.setVisible(true);
        historyBtn.setVisible(true);
        checkBalanceBtn.setVisible(true); 
        requestChequeBtn.setVisible(true);
        logoutBtn.setVisible(true);

        viewCustomersBtn.setVisible(false);
        processChequeBtn.setVisible(false);
    }

    private static void setManagerUI() {
        createBtn.setVisible(false);
        loginBtn.setVisible(false);
        managerLoginBtn.setVisible(false);

        depositBtn.setVisible(false);
        withdrawBtn.setVisible(false);
        historyBtn.setVisible(false);
        checkBalanceBtn.setVisible(false); 
        requestChequeBtn.setVisible(false);
        logoutBtn.setVisible(true);

        viewCustomersBtn.setVisible(true);
        processChequeBtn.setVisible(true);
    }

    private static void setLoggedOutUI() {
        createBtn.setVisible(true);
        loginBtn.setVisible(true);
        managerLoginBtn.setVisible(true);

        depositBtn.setVisible(false);
        withdrawBtn.setVisible(false);
        historyBtn.setVisible(false);
        checkBalanceBtn.setVisible(false); 
        requestChequeBtn.setVisible(false);
        logoutBtn.setVisible(false);
        viewCustomersBtn.setVisible(false);
        processChequeBtn.setVisible(false);
    }

    private static boolean isEmpty(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) return true;
        }
        return false;
    }

    private static void clearFields() {
        tAcc.setText("");
        tName.setText("");
        tEmail.setText("");
        tPhone.setText("");
        tAmt.setText("");
    }
}