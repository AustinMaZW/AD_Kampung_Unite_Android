package com.example.ad_project_kampung_unite.entities;

public class CombinedPurchaseList {
	private int id;
	private int productId;
	private int quantity;
	private double productSubtotal;
	private double productDiscount;
	private double productUnitPrice;
	private GroupPlan groupPlan;
	private String productName;
	private Product product;

	public CombinedPurchaseList() {
	}

	public CombinedPurchaseList(int id, int productId, int quantity, double productSubtotal, double productDiscount, double productUnitPrice, GroupPlan groupPlan) {
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.productSubtotal = productSubtotal;
		this.productDiscount = productDiscount;
		this.productUnitPrice = productUnitPrice;
		this.groupPlan = groupPlan;
	}

	public CombinedPurchaseList(int id, int productId, String productName, int quantity){
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getProductSubtotal() {
		return productSubtotal;
	}

	public void setProductSubtotal(double productSubtotal) {
		this.productSubtotal = productSubtotal;
	}

	public double getProductDiscount() { return productDiscount; }

	public void setProductDiscount(double productDiscount) { this.productDiscount = productDiscount; }

	public double getProductUnitPrice() {
		return productUnitPrice;
	}

	public void setProductUnitPrice(double productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}

	public GroupPlan getGroupPlan() {
		return groupPlan;
	}

	public void setGroupPlan(GroupPlan groupPlan) {
		this.groupPlan = groupPlan;
	}

	public Product getProduct() { return product; }

	public void setProduct(Product product) { this.product = product; }
}
