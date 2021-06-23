package za.co.garycampbell.mysticbrewuser.models;

public class OrdersModel {

    String userID,
            userName,
            userEmail,
            userMobile,
            productQuantity,
            productName,
            orderID,
            orderStatus,
            onHold,
            onHoldReason,
            isWaiting,
            hasUnpackedProducts,
            cartID,
            businessID,
            ProductCartQuantity,
            productImage,
            productID,
            serviceID,
            productPrice,
            productCategory,
            hasPaid,
            paidStatusVerified,
            verificationID;

    public OrdersModel() {
    }

    public OrdersModel(String userID, String userName, String userEmail, String userMobile, String productQuantity, String productName, String orderID, String orderStatus, String onHold, String onHoldReason, String isWaiting, String hasUnpackedProducts, String cartID, String businessID, String productCartQuantity, String productImage, String productID, String serviceID, String productPrice, String productCategory, String hasPaid, String paidStatusVerified, String verificationID) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.productQuantity = productQuantity;
        this.productName = productName;
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.onHold = onHold;
        this.onHoldReason = onHoldReason;
        this.isWaiting = isWaiting;
        this.hasUnpackedProducts = hasUnpackedProducts;
        this.cartID = cartID;
        this.businessID = businessID;
        ProductCartQuantity = productCartQuantity;
        this.productImage = productImage;
        this.productID = productID;
        this.serviceID = serviceID;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.hasPaid = hasPaid;
        this.paidStatusVerified = paidStatusVerified;
        this.verificationID = verificationID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOnHold() {
        return onHold;
    }

    public void setOnHold(String onHold) {
        this.onHold = onHold;
    }

    public String getOnHoldReason() {
        return onHoldReason;
    }

    public void setOnHoldReason(String onHoldReason) {
        this.onHoldReason = onHoldReason;
    }

    public String getIsWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(String isWaiting) {
        this.isWaiting = isWaiting;
    }

    public String getHasUnpackedProducts() {
        return hasUnpackedProducts;
    }

    public void setHasUnpackedProducts(String hasUnpackedProducts) {
        this.hasUnpackedProducts = hasUnpackedProducts;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getProductCartQuantity() {
        return ProductCartQuantity;
    }

    public void setProductCartQuantity(String productCartQuantity) {
        ProductCartQuantity = productCartQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(String hasPaid) {
        this.hasPaid = hasPaid;
    }

    public String getPaidStatusVerified() {
        return paidStatusVerified;
    }

    public void setPaidStatusVerified(String paidStatusVerified) {
        this.paidStatusVerified = paidStatusVerified;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }
}

