package com.example.chen.final_project;

/**
 * Created by zeyan on 2018-04-13.
 */

public class Intake_Form_Patient_Info {

    public String paient_id;
    public String name;
    public String address;
    public String birthday;
    public String phone;
    public String healthcard;
    public String description;
    public String surgery;
    public String allergies;
    public String glassbought;
    public String glassstore;
    public String beneift;
    public String hadbrace;

    public Intake_Form_Patient_Info(){
        //default constructor
    }

    public Intake_Form_Patient_Info(String name, String address){
        setName(name);
        setAddress(address);
    }

    public String getPaient_id() {
        return paient_id;
    }
    public String getName() {
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getHealthcard() {
        return healthcard;
    }

    public String getDescription() {
        return description;
    }

    public String getSurgery() {
        return surgery;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getGlassbought() {
        return glassbought;
    }

    public String getGlassstore() {
        return glassstore;
    }

    public String getBeneift() {
        return beneift;
    }

    public String getHadbrace() {
        return hadbrace;
    }


    public void setPaient_id(String paient_id) {
        this.paient_id = paient_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHealthcard(String healthcard) {
        this.healthcard = healthcard;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSurgery(String surgery) {
        this.surgery = surgery;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setGlassbought(String glassbought) {
        this.glassbought = glassbought;
    }

    public void setGlassstore(String glassstore) {
        this.glassstore = glassstore;
    }

    public void setBeneift(String beneift) {
        this.beneift = beneift;
    }

    public void setHadbrace(String hadbrace) {
        this.hadbrace = hadbrace;
    }
}
