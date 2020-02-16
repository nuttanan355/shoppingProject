package com.example.shoppingproject.Model;

public class Cart {

//    private String pid,pname,price,quantity,discount,image;
    private String ProductID,ProductName,ProductPrice,quantity,ProductDescription,ProductImage;

    public Cart() {
    }

    public Cart(String productID, String productName, String productPrice, String quantity, String productDescription, String productImage) {
        ProductID = productID;
        ProductName = productName;
        ProductPrice = productPrice;
        this.quantity = quantity;
        ProductDescription = productDescription;
        ProductImage = productImage;
    }



    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }
}
