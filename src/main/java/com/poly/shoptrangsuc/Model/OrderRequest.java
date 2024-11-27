package com.poly.shoptrangsuc.Model;

public class OrderRequest {

    private Account account;
    private double totalAmount;

    // Getters and Setters

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
