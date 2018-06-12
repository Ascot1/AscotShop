package com.ascot.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.ascot.domain.Category;
import com.ascot.domain.Product;
import com.ascot.utils.DataSourceUtils;

public class ProductDao {

	//获取热门商品
	public List<Product> findHotProduct() throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";//限制所查询的商品信息为9条
		return qr.query(sql, new BeanListHandler<Product>(Product.class), 1,0,9);
	}

	//获取最新商品
	public List<Product> findNewProduct() throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,? ";
		return qr.query(sql,new BeanListHandler<Product>(Product.class), 0,9);
	}
   //获取该cid对应的所有商品数
	public int getCount(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from product where cid=?";
        Long totalRecord = (Long)qr.query(sql, new ScalarHandler(),cid);		
		return totalRecord.intValue();
	}
    //获取该cid对应的所有商品
	public List<Product> findProductListByCid(String cid, int index, int pageSize) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where cid=? limit ?,?";
		List<Product> productList = qr.query(sql, new BeanListHandler<Product>(Product.class),cid,index,pageSize);
		return productList;
	}

	//获取category
	public Category findProductCategory(String cid) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category where cid=? ";
		return qr.query(sql, new BeanHandler<Category>(Category.class),cid);	
	}
    
	//获取pid对应的商品
	public Product getProductBypid(String pid) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql ="select * from product where pid = ?";
        return qr.query(sql, new BeanHandler<Product>(Product.class),pid);
	}


}
