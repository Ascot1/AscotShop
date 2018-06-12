package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.service.UserService;


public class CheckUsernameServlet extends HttpServlet {
	
	boolean isExit = false;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得请求数据
		String username = request.getParameter("username");
		UserService service = new UserService();
		try {
			isExit = service.checkUsername(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将数据封装为json格式并跳转到页面(这里的格式有点疑问？)
		String json = "{\"isExit\":"+isExit+"}";
		response.getWriter().write(json);
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
