package com.dsa.project;

import java.util.Stack;

public class Customer {

    private final int accountNo;
    private final String name;
    private final String email;
    private final String phone;
    private double balance;
    private final Stack<String> transactionHistory = new Stack<>();

    public Customer(int accountNo, String name, String email, String phone, double balance) {
        this.accountNo = accountNo;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
    }

    public int getAccountNo() { return accountNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }

    public Stack<String> getTransactionHistory() {
        Stack<String> copy = new Stack<>();
        copy.addAll(transactionHistory);
        return copy;
    }

    public void setBalance(double balance) { this.balance = balance; }
    public void addTransaction(String message) { transactionHistory.push(message); }
}