package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.domain.Product;
import com.ascot.service.ProductService;



public class IndexServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//获取热门商品
	ProductService service =  new ProductService();
	List<Product>hotProduct = null;
	List<Product>newProduct = null;
	try {
		hotProduct =  service.findHotProduct();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
    //获取最新商品
	try {
		newProduct =  service.findNewProduct();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
	//将商品放于request域中
	request.setAttribute("hotProduct", hotProduct);
	request.setAttribute("newProduct", newProduct);
	//将商品信息发送到页面
	request.getRequestDispatcher("/index.jsp").forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
