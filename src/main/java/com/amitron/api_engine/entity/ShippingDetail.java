/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.amitron.api_engine.entity;

/**
 *
 * @author Ngn
 */
public class ShippingDetail {

    private Object packingSlipNumber;
    private Object packingSlipDate;
    private Object carrier;
    private Object custDockDate;
    private Object trackingID;
    private Object bookDate;

    // getters/setters
    public Object getPackingSlipNumber() { return packingSlipNumber; }
    public void setPackingSlipNumber(Object packingSlipNumber) { this.packingSlipNumber = packingSlipNumber; }

    public Object getPackingSlipDate() { return packingSlipDate; }
    public void setPackingSlipDate(Object packingSlipDate) { this.packingSlipDate = packingSlipDate; }

    public Object getCarrier() { return carrier; }
    public void setCarrier(Object carrier) { this.carrier = carrier; }

    public Object getCustDockDate() { return custDockDate; }
    public void setCustDockDate(Object custDockDate) { this.custDockDate = custDockDate; }

    public Object getTrackingID() { return trackingID; }
    public void setTrackingID(Object trackingID) { this.trackingID = trackingID; }

    public Object getBookDate() { return bookDate; }
    public void setBookDate(Object bookDate) { this.bookDate = bookDate; }
}
