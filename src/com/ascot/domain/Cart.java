package com.ascot.domain;

import java.util.HashMap;
import java.util.Map;

//购物车
public class Cart {
	private Map<String, CartItem> cartItem = new HashMap<String, CartItem>();// 购物车项
	private double total;// 总计

	public Map<String, CartItem> getCartItem() {
		return cartItem;
	}

	public void setCartItem(Map<String, CartItem> cartItem) {
		this.cartItem = cartItem;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
  
  
}
