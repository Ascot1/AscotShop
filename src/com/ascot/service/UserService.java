package com.ascot.service;

import java.sql.SQLException;

import com.ascot.dao.UserDao;
import com.ascot.domain.User;

public class UserService {

	long count = 0L;
	public boolean register(User user) throws SQLException {
		UserDao dao = new UserDao();
		int row = 0;
		 row = dao.register(user);
		return row>0?true:false;
	}

	//用户的激活
	public void active(String activeCode) throws SQLException {
		UserDao dao = new UserDao();
		dao.active(activeCode);
		
	}
   //验证用户名是否存在
	public boolean checkUsername(String username) throws SQLException {
		UserDao dao = new UserDao();
		count =  dao.checkUsername(username);
		return count>0?true:false;
	}

}
