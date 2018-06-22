package com.ascot.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//订单
public class Order {
  
	private String oid;
	private Date ordertime;//订单确认时间
	private double total;//订单总计
	private int state;//订单是否支付，为0表示未支付，为1表示支付
	private String address;//订收货地址
	private String name;//收货人姓名
	private String telephone;//收货人电话
	private User user;//订单用户
	
	//订单里需要包含订单项
     List<OrderItem> orderItem = new ArrayList<OrderItem>();
	
	
	public List<OrderItem> getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
