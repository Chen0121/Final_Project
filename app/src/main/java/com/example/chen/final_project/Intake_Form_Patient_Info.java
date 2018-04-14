package com.example.chen.final_project;

/**
 * Created by zeyan on 2018-04-13.
 */

public class Intake_Form_Patient_Info {

    public String name;
    public String address;

    public Intake_Form_Patient_Info(){
        //default constructor
    }

    public Intake_Form_Patient_Info(String name, String address){
        setName(name);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public String getAddress(){
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
