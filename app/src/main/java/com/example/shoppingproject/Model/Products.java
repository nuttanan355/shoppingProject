package com.example.shoppingproject.Model;

public class Products
{

    private String ProductID,ProductName,ProductCategory,ProductDescription,ProductPrice,ProductImage,ProductDate,ProductTime;

    public Products()
    {


    }

    public Products(String productID, String productName, String productCategory, String productDescription, String productPrice, String productImage, String productDate, String productTime) {
        ProductID = productID;
        ProductName = productName;
        ProductCategory = productCategory;
        ProductDescription = productDescription;
        ProductPrice = productPrice;
        ProductImage = productImage;
        ProductDate = productDate;
        ProductTime = productTime;
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

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductDate() {
        return ProductDate;
    }

    public void setProductDate(String productDate) {
        ProductDate = productDate;
    }

    public String getProductTime() {
        return ProductTime;
    }

    public void setProductTime(String productTime) {
        ProductTime = productTime;
    }
}
