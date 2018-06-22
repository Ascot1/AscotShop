package com.ascot.domain;

//购物车项
public class CartItem {
	private Product product;// 商品
	private int buyNum;// 商品数目
	private double subtotal;// 商品小计

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

}
