package za.co.garycampbell.mysticbrewuser.models;


import java.util.HashMap;

public class ProductModel  {

     public String
             vendorID,
             iD,
             name,
            shortDescription,
            longDescription,
            price,
            quantity,
            category,
            xxxlSizeQuantity,
            xxlSizeQuantity,
            xlSizeQuantity,
            largeSizeQuantity,
            mediumSizeQuantity,
            smallSizeQuantity,
            dateAvailable,
            productWeight,
            productLength,
            productWidth,
            productHeight;

    public boolean
            hasSize,
            isAvailable,
            colors;

    public String imageUrl;


    public ProductModel() {
    }

    public ProductModel(String vendorID, String iD, String name, String shortDescription, String longDescription, String price, String quantity, String category, String xxxlSizeQuantity, String xxlSizeQuantity, String xlSizeQuantity, String largeSizeQuantity, String mediumSizeQuantity, String smallSizeQuantity, String dateAvailable, String productWeight, String productLength, String productWidth, String productHeight, boolean hasSize, boolean isAvailable, boolean colors, String imageUrl) {
        this.vendorID = vendorID;
        this.iD = iD;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.xxxlSizeQuantity = xxxlSizeQuantity;
        this.xxlSizeQuantity = xxlSizeQuantity;
        this.xlSizeQuantity = xlSizeQuantity;
        this.largeSizeQuantity = largeSizeQuantity;
        this.mediumSizeQuantity = mediumSizeQuantity;
        this.smallSizeQuantity = smallSizeQuantity;
        this.dateAvailable = dateAvailable;
        this.productWeight = productWeight;
        this.productLength = productLength;
        this.productWidth = productWidth;
        this.productHeight = productHeight;
        this.hasSize = hasSize;
        this.isAvailable = isAvailable;
        this.colors = colors;
        this.imageUrl = imageUrl;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getXxxlSizeQuantity() {
        return xxxlSizeQuantity;
    }

    public void setXxxlSizeQuantity(String xxxlSizeQuantity) {
        this.xxxlSizeQuantity = xxxlSizeQuantity;
    }

    public String getXxlSizeQuantity() {
        return xxlSizeQuantity;
    }

    public void setXxlSizeQuantity(String xxlSizeQuantity) {
        this.xxlSizeQuantity = xxlSizeQuantity;
    }

    public String getXlSizeQuantity() {
        return xlSizeQuantity;
    }

    public void setXlSizeQuantity(String xlSizeQuantity) {
        this.xlSizeQuantity = xlSizeQuantity;
    }

    public String getLargeSizeQuantity() {
        return largeSizeQuantity;
    }

    public void setLargeSizeQuantity(String largeSizeQuantity) {
        this.largeSizeQuantity = largeSizeQuantity;
    }

    public String getMediumSizeQuantity() {
        return mediumSizeQuantity;
    }

    public void setMediumSizeQuantity(String mediumSizeQuantity) {
        this.mediumSizeQuantity = mediumSizeQuantity;
    }

    public String getSmallSizeQuantity() {
        return smallSizeQuantity;
    }

    public void setSmallSizeQuantity(String smallSizeQuantity) {
        this.smallSizeQuantity = smallSizeQuantity;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(String dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductLength() {
        return productLength;
    }

    public void setProductLength(String productLength) {
        this.productLength = productLength;
    }

    public String getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(String productWidth) {
        this.productWidth = productWidth;
    }

    public String getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(String productHeight) {
        this.productHeight = productHeight;
    }

    public boolean isHasSize() {
        return hasSize;
    }

    public void setHasSize(boolean hasSize) {
        this.hasSize = hasSize;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isColors() {
        return colors;
    }

    public void setColors(boolean colors) {
        this.colors = colors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
