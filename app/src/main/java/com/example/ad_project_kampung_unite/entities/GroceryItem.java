package com.example.ad_project_kampung_unite.entities;

import java.io.Serializable;

public class GroceryItem implements Serializable {
	private int id;
	private int quantity;
	private double subtotal;
	private Product product;
	private GroceryList groceryList;


	public GroceryItem(int id, int quantity, double subtotal, Product product, GroceryList groceryList) {
		this.id = id;
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.product = product;
		this.groceryList = groceryList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public GroceryList getGroceryList() {
		return groceryList;
	}

	public void setGroceryList(GroceryList groceryList) {
		this.groceryList = groceryList;
	}
}
