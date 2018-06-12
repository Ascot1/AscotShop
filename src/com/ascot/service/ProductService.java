package com.ascot.service;

import java.sql.SQLException;
import java.util.List;

import com.ascot.dao.ProductDao;
import com.ascot.domain.Category;
import com.ascot.domain.PageBean;
import com.ascot.domain.Product;

public class ProductService {

	//获取热门商品
	public List<Product> findHotProduct() throws SQLException {
		ProductDao dao = new ProductDao();
		return dao.findHotProduct();
	}

	//获取最新商品（根据时间来确定）
	public List<Product> findNewProduct() throws SQLException {
		ProductDao dao = new ProductDao();
		return dao.findNewProduct();
	}
    //获取所属Cid的所有商品并进行分页
	public PageBean findProductListByCid(String cid,int currentPage,int pageSize) {
		ProductDao dao = new ProductDao();
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		//1.封装当前页
		pageBean.setCurrentPage(currentPage);
		//2.封装每页显示的条数
		pageBean.setPageSize(pageSize);
		//3.封装总记录（根据cid筛选）		
		int totalRecord = 0;
		try {
			totalRecord = dao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalRecord(totalRecord);
		//4.封装总分页数
		int totalPage = (int) Math.ceil(1.0*totalRecord/pageSize);
	    pageBean.setTotalPage(totalPage);
		//5.封装每页中的数据
	    int index = (currentPage-1)*pageSize;
	    List<Product> data = null;
		try {
		    data = dao.findProductListByCid(cid,index,pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setData(data);
		return pageBean;
	}
   //获取类别category
	public Category findProductCategory(String cid) throws SQLException {
		ProductDao dao = new ProductDao();
		return dao.findProductCategory(cid);
		
	}
    
	//根据Pid获取商品信息
	public Product getProductByPid(String pid) {
		ProductDao dao = new ProductDao();
		Product	product = null;
		try {
		product = dao.getProductBypid(pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return product;	
	}


}
