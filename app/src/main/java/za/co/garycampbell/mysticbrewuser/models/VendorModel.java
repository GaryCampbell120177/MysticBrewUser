package za.co.garycampbell.mysticbrewuser.models;


import java.security.Key;
import java.util.HashMap;

public class VendorModel {

    public String businessID, businessName, businessAddress, businessContactPerson, businessContactPersonMobile, businessEmail, businessContactNumber, businessCategory, _password;
    public Double businessLatitude, businessLongitude;
    public String ImageUrl;
    boolean hasPaid;

    public VendorModel() {
    }

    public VendorModel(String businessID, String businessName, String businessAddress, String businessContactPerson, String businessContactPersonMobile, String businessEmail, String businessContactNumber, String businessCategory, String _password, Double businessLatitude, Double businessLongitude, String vendorImageUrl, boolean hasPaid) {
        this.businessID = businessID;
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.businessContactPerson = businessContactPerson;
        this.businessContactPersonMobile = businessContactPersonMobile;
        this.businessEmail = businessEmail;
        this.businessContactNumber = businessContactNumber;
        this.businessCategory = businessCategory;
        this._password = _password;
        this.businessLatitude = businessLatitude;
        this.businessLongitude = businessLongitude;
        this.ImageUrl = vendorImageUrl;
        this.hasPaid = hasPaid;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessContactPerson() {
        return businessContactPerson;
    }

    public void setBusinessContactPerson(String businessContactPerson) {
        this.businessContactPerson = businessContactPerson;
    }

    public String getBusinessContactPersonMobile() {
        return businessContactPersonMobile;
    }

    public void setBusinessContactPersonMobile(String businessContactPersonMobile) {
        this.businessContactPersonMobile = businessContactPersonMobile;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessContactNumber() {
        return businessContactNumber;
    }

    public void setBusinessContactNumber(String businessContactNumber) {
        this.businessContactNumber = businessContactNumber;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public Double getBusinessLatitude() {
        return businessLatitude;
    }

    public void setBusinessLatitude(Double businessLatitude) {
        this.businessLatitude = businessLatitude;
    }

    public Double getBusinessLongitude() {
        return businessLongitude;
    }

    public void setBusinessLongitude(Double businessLongitude) {
        this.businessLongitude = businessLongitude;
    }

    public String getVendorImageUrl() {
        return ImageUrl;
    }

    public void setVendorImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

}
