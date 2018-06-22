package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ascot.domain.User;
import com.ascot.service.UserService;


public class LoginServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		//获取登陆账号及密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//判断数据库中是否存在该用户
		User user = null;
		UserService service = new UserService();
		try {
			user = service.login(username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//如果用户存在
		if(user!=null) {
			//判断用户是否勾选了自动登陆
			String autologin = request.getParameter("autologin");
			if("true".equals(autologin)) {
              //创建存储登陆名和密码的cookie
			  Cookie cookie_username = new Cookie("username",username);
			  cookie_username.setMaxAge(60*10);
			  Cookie cookie_password = new Cookie("password",password);
			  cookie_password.setMaxAge(60*10);
			  //将其存储到用户客户端的cookie中
			  response.addCookie(cookie_username);
			  response.addCookie(cookie_password);
			}
			//将user对象存储到session中
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
			//跳转到主界面(重定向到首页)url会改变
			response.sendRedirect(request.getContextPath()+"/product?method=index");
		}
		else {
			request.setAttribute("errorLogin","输入的用户名及密码错误！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
