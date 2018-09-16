package com.atschoolPioneerSchool.model;

import java.io.Serializable;

/**
 * Created by user on 25/05/2017.
 */

public class Statement implements Serializable {
    private long id;

    public int Message_Id;
    public String msg;
    public String msgA;
    public double TRANS_AMOUNT_DEBIT;
    public double TRANS_AMOUNT_CREDIT;
    public double TOTAL_TRANS_AMOUNT_DEBIT;
    public double TOTAL_TRANS_AMOUNT_CREDIT;
    public double Balance;
    public String ACCOUNTS_NAMEA;
    public String ACCOUNTS_NAME;
    public String ACC_TRANS_TYPE_NAME;
    public String ACC_TRANS_TYPE_NAMEA;
    public String Date_G;
    public String Book_Voucher_No;

    public boolean LastObject = false;

    public Statement() {

    }

    public Statement(long id, int Message_Id, String msg, String msgA, double TRANS_AMOUNT_DEBIT, double TRANS_AMOUNT_CREDIT
            , double TOTAL_TRANS_AMOUNT_DEBIT, double TOTAL_TRANS_AMOUNT_CREDIT, double Balance
            , String ACCOUNTS_NAMEA, String ACCOUNTS_NAME, String ACC_TRANS_TYPE_NAME, String ACC_TRANS_TYPE_NAMEA
            , String Date_G, String Book_Voucher_No, boolean LastObject) {

        this.LastObject = LastObject;
        this.id = id;
        this.Message_Id = Message_Id;
        this.msg = msg;
        this.msgA = msgA;
        this.TRANS_AMOUNT_DEBIT = TRANS_AMOUNT_DEBIT;
        this.TRANS_AMOUNT_CREDIT = TRANS_AMOUNT_CREDIT;
        this.TOTAL_TRANS_AMOUNT_DEBIT = TOTAL_TRANS_AMOUNT_DEBIT;
        this.TOTAL_TRANS_AMOUNT_CREDIT = TOTAL_TRANS_AMOUNT_CREDIT;
        this.Balance = Balance;
        this.ACCOUNTS_NAMEA = ACCOUNTS_NAMEA;
        this.ACCOUNTS_NAME = ACCOUNTS_NAME;
        this.ACC_TRANS_TYPE_NAME = ACC_TRANS_TYPE_NAME;
        this.ACC_TRANS_TYPE_NAMEA = ACC_TRANS_TYPE_NAMEA;
        this.Date_G = Date_G;
        this.Book_Voucher_No = Book_Voucher_No;
    }

    public long getId() {
        return id;
    }
}