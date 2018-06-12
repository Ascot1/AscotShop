package com.ascot.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.ascot.domain.User;
import com.ascot.utils.DataSourceUtils;

public class UserDao {

	public int register(User user) throws SQLException {
		// 将注册信息存入数据库
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		int update = qr.update(sql, user.getUid(),user.getUsername(),user.getPassword(),user.getName(),
				user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode());
		return update;
	}
      //激活
	public void active(String activeCode) throws SQLException {
	   QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
	   String sql = "update user set state =? where code=?";
	   qr.update(sql,1,activeCode);
	}
	//验证用户是否存在
	public long checkUsername(String username) throws SQLException {
       QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
       String sql = "select count(*) from user where username=?";
       long count = (long)qr.query(sql,new ScalarHandler(),username);
		return count;
	}

}
