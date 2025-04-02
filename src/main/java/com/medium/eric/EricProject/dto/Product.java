package com.medium.eric.EricProject.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Product {

    @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private String productName;
    private Integer productCost;
    private boolean isProductAvailable;
    private Role role; // Add role field
    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	// Constructors, getters, setters, toString
    public Product() {}

    public Product(String productName, Integer productCost, boolean isProductAvailable) {
        this.productName = productName;
        this.productCost = productCost;
        this.isProductAvailable = isProductAvailable;
    }
    public Product(Integer productId,String productName, Integer productCost, boolean isProductAvailable) {
        this.productId=productId;
    	this.productName = productName;
        this.productCost = productCost;
        this.isProductAvailable = isProductAvailable;
    }
    public Product(Integer productId, String productName, Integer productCost, boolean isProductAvailable, Role role) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productCost = productCost;
		this.isProductAvailable = isProductAvailable;
		this.role = role;
	}

	public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductCost() {
        return productCost;
    }

    public void setProductCost(Integer productCost) {
        this.productCost = productCost;
    }

    public boolean isProductAvailable() {
        return isProductAvailable;
    }

    public void setProductAvailable(boolean isProductAvailable) {
        this.isProductAvailable = isProductAvailable;
    }

    @Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productCost=" + productCost
				+ ", isProductAvailable=" + isProductAvailable + ", role=" + role + "]";
	}
}

