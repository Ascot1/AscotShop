package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.domain.Category;
import com.ascot.service.CategoryService;
import com.google.gson.Gson;


public class CategoryServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取Category数据
		CategoryService service = new CategoryService();
		List<Category> categoryList = null;
		try {
		    categoryList = service.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将集合封装为json
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		response.setCharacterEncoding("utf-8");
		//将json返回页面
		response.getWriter().write(json);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
