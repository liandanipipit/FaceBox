package com.pipitliandani.android.facebox;

/**
 * Created by User on 24/05/2018.
 */

public class FaceBoxModel {
    public String key;
    public String image_url;
    public String name;
    public Long nik;
    public String unit;
    public String workUnit;
    public String functionTitle;
    public String email;
    public String placeOfBirth;
    public String eduLevel;
    public String major;
    public String phone;

    public FaceBoxModel() {
    }

    public FaceBoxModel(String key, String image_url, String name, Long nik, String unit, String workUnit, String functionTitle, String email, String placeOfBirth, String eduLevel, String major, String phone) {
        this.key = key;
        this.image_url = image_url;
        this.name = name;
        this.nik = nik;
        this.unit = unit;
        this.workUnit = workUnit;
        this.functionTitle = functionTitle;
        this.email = email;
        this.placeOfBirth = placeOfBirth;
        this.eduLevel = eduLevel;
        this.major = major;
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNik() {
        return nik;
    }

    public void setNik(Long nik) {
        this.nik = nik;
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

    public String getFunctionTitle() {
        return functionTitle;
    }

    public void setFunctionTitle(String functionTitle) {
        this.functionTitle = functionTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getEduLevel() {
        return eduLevel;
    }

    public void setEduLevel(String eduLevel) {
        this.eduLevel = eduLevel;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
