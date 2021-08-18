package com.example.ad_project_kampung_unite.entities;

public class CombinedPurchaseList {
	private int id;
	private int productId;
	private int quantity;
	private double productSubtotal;
	private double productUnitPrice;
	private GroupPlan groupPlan;

	public CombinedPurchaseList() {
	}

	public CombinedPurchaseList(int id, int productId, int quantity, double productSubtotal, double productUnitPrice, GroupPlan groupPlan) {
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.productSubtotal = productSubtotal;
		this.productUnitPrice = productUnitPrice;
		this.groupPlan = groupPlan;
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
}
