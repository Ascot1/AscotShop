package com.ascot.service;

import java.sql.SQLException;
import java.util.List;

import com.ascot.dao.CategoryDao;
import com.ascot.domain.Category;

public class CategoryService {

	//获取类别列表
	public List<Category> findAllCategory() throws SQLException {
		CategoryDao dao = new CategoryDao();
		return dao.findAllCategory();
		
	}

}
