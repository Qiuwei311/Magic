package com.five.bill.bean;

import java.io.Serializable;
import java.util.Date;

public class Bill implements Serializable {

    /**
     * 账单 信息
     */
    private static final long serialVersionUID = 4528705706013750458L;

    private int bill_id; // 账单ID
    private int bill_type; // 账单类型， 如：支出，还款，
    private int card_id; // 卡片Id
    private int section; // 月份
    private Date bill_date;// 记账日期
    private String card_number; // 卡号    
    private String description; // 账单描述

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public int getBill_type() {
        return bill_type;
    }

    public void setBill_type(int bill_type) {
        this.bill_type = bill_type;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public Date getBill_date() {
        return bill_date;
    }

    public void setBill_date(Date bill_date) {
        this.bill_date = bill_date;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "Bill [bill_id=" + bill_id + ", bill_type=" + bill_type
                + ", card_id=" + card_id + ", bill_date=" + bill_date
                + ", card_number=" + card_number + ", section=" + section
                + ", description=" + description + "]";
    }

}
