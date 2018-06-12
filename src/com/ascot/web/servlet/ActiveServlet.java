package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.service.UserService;


public class ActiveServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取激活码
		String activeCode =  request.getParameter("activeCode");
		UserService service = new UserService();
		try {
			service.active(activeCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//假定都激活成功，跳转到登陆界面
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
