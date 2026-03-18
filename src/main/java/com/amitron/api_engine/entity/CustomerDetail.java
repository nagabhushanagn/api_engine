/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.entity;

/**
 *
 * @author Ngn
 */
public class CustomerDetail {

    private String customerName;
    private String customerNumber;
    private Object billAddress;
    private Object city;
    private Object state;
    private Object country;
    private Object zipCode;
    private Object phoneCode;
    private Object phoneNumber;
    private Object email;
    private Object term;

    // Getters and Setters

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public Object getBillAddress() { return billAddress; }
    public void setBillAddress(Object billAddress) { this.billAddress = billAddress; }

    public Object getCity() { return city; }
    public void setCity(Object city) { this.city = city; }

    public Object getState() { return state; }
    public void setState(Object state) { this.state = state; }

    public Object getCountry() { return country; }
    public void setCountry(Object country) { this.country = country; }

    public Object getZipCode() { return zipCode; }
    public void setZipCode(Object zipCode) { this.zipCode = zipCode; }

    public Object getPhoneCode() { return phoneCode; }
    public void setPhoneCode(Object phoneCode) { this.phoneCode = phoneCode; }

    public Object getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Object phoneNumber) { this.phoneNumber = phoneNumber; }

    public Object getEmail() { return email; }
    public void setEmail(Object email) { this.email = email; }

    public Object getTerm() { return term; }
    public void setTerm(Object term) { this.term = term; }
}
