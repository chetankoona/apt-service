package com.apt.aptservice;

public class BankTransaction {
    String date;
    String tranid;
    String remarks;
    Double deposit;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "date='" + date + '\'' +
                ", tranid='" + tranid + '\'' +
                ", remarks='" + remarks + '\'' +
                ", deposit=" + deposit +
                '}';
    }
}
