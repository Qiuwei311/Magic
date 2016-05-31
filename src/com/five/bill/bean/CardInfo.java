package com.five.bill.bean;

import java.io.Serializable;
import java.util.Date;

public class CardInfo implements Serializable {

    /**
     *  卡片  信息
     */
    private static final long serialVersionUID = 4613932056978162541L;
    
    private int card_id;            // id
    private String bank_name;       // name
    private String card_number;     // 卡号
    private double unsettled_bills; // 未出
    private double settled_bills;   // 已出
    private double current_debt;    // 当前欠款
    private String statement_date;  // 账单日期
    private String payment_date;    // 还款日期
    private String notity_date;     // 提醒日
    private boolean need_notity;    // 设置了提醒

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public double getUnsettled_bills() {
        return unsettled_bills;
    }

    public void setUnsettled_bills(double unsettled_bills) {
        this.unsettled_bills = unsettled_bills;
    }

    public double getSettled_bills() {
        return settled_bills;
    }

    public void setSettled_bills(double settled_bills) {
        this.settled_bills = settled_bills;
    }

    public double getCurrent_debt() {
        return current_debt;
    }

    public void setCurrent_debt(double current_debt) {
        this.current_debt = current_debt;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getStatement_date() {
        return statement_date;
    }

    public void setStatement_date(String statement_date) {
        this.statement_date = statement_date;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getNotity_date() {
        return notity_date;
    }

    public void setNotity_date(String notity_date) {
        this.notity_date = notity_date;
    }

    public boolean isNeed_notity() {
        return need_notity;
    }

    public void setNeed_notity(boolean need_notity) {
        this.need_notity = need_notity;
    }

    @Override
    public String toString() {
        return "CardInfo [card_id=" + card_id + ", bank_name=" + bank_name
                + ", card_number=" + card_number + ", unsettled_bills="
                + unsettled_bills + ", settled_bills=" + settled_bills
                + ", current_debt=" + current_debt + ", statement_date="
                + statement_date + ", payment_date=" + payment_date
                + ", notity_date=" + notity_date + ", need_notity="
                + need_notity + "]";
    }
    
    

}
