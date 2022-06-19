package com.apt.aptservice;

public class MaintenanceReport {
    String flat;
    String paidDate;
    Double amount;

    public MaintenanceReport() {
    }

    public MaintenanceReport(String flat, String paidDate, Double amount) {
        this.flat = flat;
        this.paidDate = paidDate;
        this.amount = amount;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MaintenanceReport{" +
                "flat='" + flat + '\'' +
                ", paidDate='" + paidDate + '\'' +
                ", amount=" + amount +
                '}';
    }
}
