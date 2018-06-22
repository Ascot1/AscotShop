package com.ascot.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ascot.dao.ProductDao;
import com.ascot.domain.Category;
import com.ascot.domain.Order;
import com.ascot.domain.PageBean;
import com.ascot.domain.Product;
import com.ascot.utils.DataSourceUtils;

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

	
	//将订单添加到数据库
	public void submitOrder(Order order) {
        ProductDao dao = new ProductDao();
       
        try {
        	//开启事务
			DataSourceUtils.startTransaction();
			dao.submitOrder(order);
			dao.submitOrderItem(order);
           			
		} catch (SQLException e) {
           try {
        	//回滚事务
			DataSourceUtils.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		e.printStackTrace();
		}finally {
			//提交事务
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	//更新收货人信息
	public void updateOrderAdrr(Order order) {
        ProductDao dao = new ProductDao();
        try {
			dao.updateOrderAdrr(order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
    //支付成功之后修改state状态值
	public void setState(String r6_Order) {
        ProductDao dao = new ProductDao();
        try {
			dao.setState(r6_Order);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	//获取所有订单
	public List<Order> findAllOrders(String uid) {
        ProductDao dao = new ProductDao();
        List<Order> orderList = null;
        try {
			orderList = dao.findAllOrders(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return orderList;
	}

	//获取所有的OrderItem及product中的数据
	public List<Map<String, Object>> findAllOrderItemByOid(String oid) {
		 ProductDao dao = new ProductDao();
		 List<Map<String, Object>> mapList = null;
	        try {
				mapList = dao.findAllOrderItemByOid(oid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        return mapList;
	}

	
}
