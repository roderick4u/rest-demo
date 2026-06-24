package com.restproject.rest_demo.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="cloud_vendor_info")
public class CloudVendor
{
    @Id
    private String vendorId;
    private String vendorName;
    private String vendorAdress;
    private int vendorPhoneNumber;


    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(int vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public String getVendorAdress() {
        return vendorAdress;
    }

    public void setVendorAdress(String vendorAdress) {
        this.vendorAdress = vendorAdress;
    }


    public CloudVendor() {
    }

    public CloudVendor(String vendorName, String vendorId, String vendorAdress, int vendorPhoneNumber) {
        this.vendorName = vendorName;
        this.vendorId = vendorId;
        this.vendorAdress = vendorAdress;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }
}
