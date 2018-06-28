package com.pipitliandani.android.facebox;

/**
 * Created by User on 24/05/2018.
 */

public class FaceBoxModel {
    public String birthDate;
    public String coop;
    public String eduLevel;
    public String email;
    public String functionTitle;
    public String gender;
    public String image_url;
    public String limit;
    public String major;
    public String name;
    public String nik;
    public String officials;
    public String pendionBudget;
    public String phone;
    public String placeOfBirth;
    public String unit;
    public String workUnit;
    public Long id;
    public String dateMonthBirth;


    public FaceBoxModel(String birthDate, String coop, String eduLevel, String email, String functionTitle, String gender, String image_url, String limit, String major, String name, String nik, String officials, String pendionBudget, String phone, String placeOfBirth, String unit, String workUnit, Long id, String dateMonthBirth) {
        this.birthDate = birthDate;
        this.coop = coop;
        this.eduLevel = eduLevel;
        this.email = email;
        this.functionTitle = functionTitle;
        this.gender = gender;
        this.image_url = image_url;
        this.limit = limit;
        this.major = major;
        this.name = name;
        this.nik = nik;
        this.officials = officials;
        this.pendionBudget = pendionBudget;
        this.phone = phone;
        this.placeOfBirth = placeOfBirth;
        this.unit = unit;
        this.workUnit = workUnit;
        this.id = id;
        this.dateMonthBirth = dateMonthBirth;

    }

    public FaceBoxModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCoop() {
        return coop;
    }

    public void setCoop(String coop) {
        this.coop = coop;
    }

    public String getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(String eduLevel) {
        this.eduLevel = eduLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public void setFunctionTitle(String functionTitle) {
        this.functionTitle = functionTitle;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getOfficials() {
        return officials;
    }

    public void setOfficials(String officials) {
        this.officials = officials;
    }

    public String getPendionBudget() {
        return pendionBudget;
    }

    public void setPendionBudget(String pendionBudget) {
        this.pendionBudget = pendionBudget;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getDateMonthBirth() {
        return dateMonthBirth;
    }

    public void setDateMonthBirth(String dateMonthBirth) {
        this.dateMonthBirth = dateMonthBirth;
    }
}
