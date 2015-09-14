package com.example.oem.oweme;

import java.util.ArrayList;

/**
 * Created by alex on 19/05/15.
 */
public class Contact{
    private int id;
    private String name;
    private double amount;
    private String amount_list;

    public Contact(){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amount_list = "";
    }

    public Contact(String name, double amount){
        this.name = name;
        this.amount = amount;
        this.amount_list = Double.toString(amount);
    }

    public Contact(String name, Double amount, String amount_list){
        this.name = name;
        this.amount = amount;
        this.amount_list = amount_list;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        if(this.amount_list == ""){
            this.amount_list = Double.toString(amount);
            this.amount = amount;
        } else {
            this.amount_list = amount_list.concat("," + Double.toString(amount));
            this.amount = amount;
        }
    }

    public String getAmount_list(){
        return this.amount_list;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

}
