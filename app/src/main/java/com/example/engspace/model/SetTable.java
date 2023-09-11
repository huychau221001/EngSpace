package com.example.engspace.model;

import java.util.ArrayList;
import java.util.Date;

public class SetTable {
    private Integer id;
    private Integer set;
    private String name;
    private Integer amount;

    public SetTable(Integer set, String name, Integer amount) {
        this.set = set;
        this.name = name;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSet() {
        return set;
    }

    public void setSet(Integer set) {
        this.set = set;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
