package za.co.garycampbell.mysticbrewuser.models;

public class UserProductsModel {

    String businessID, imageURL, productCategory, productName, productPrice, productQuatity, userID, vendorID, cartID;

    public UserProductsModel() {
    }

    public UserProductsModel(String businessID, String imageURL, String productCategory, String productName, String productPrice, String productQuatity, String userID, String vendorID, String cartID) {
        this.businessID = businessID;
        this.imageURL = imageURL;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuatity = productQuatity;
        this.userID = userID;
        this.vendorID = vendorID;
        this.cartID = cartID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuatity() {
        return productQuatity;
    }

    public void setProductQuatity(String productQuatity) {
        this.productQuatity = productQuatity;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }
}
