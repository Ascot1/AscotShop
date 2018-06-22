package com.ascot.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.ascot.domain.Category;
import com.ascot.domain.Order;
import com.ascot.domain.OrderItem;
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

	//将订单添加到数据库
	public void submitOrder(Order order) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into orders values (?,?,?,?,?,?,?,?)";
		Connection conn = DataSourceUtils.getConnection();
		qr.update(conn, sql, order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),order.getAddress(),
				order.getName(),order.getTelephone(),order.getUser().getUid());
	}

	//将订单项添加到数据库
	public void submitOrderItem(Order order) throws SQLException {
       QueryRunner qr = new QueryRunner();
       List<OrderItem> orderItem = order.getOrderItem();
       Connection conn = DataSourceUtils.getConnection();
       String sql ="insert into orderitem values (?,?,?,?,?)";
       for(OrderItem item : orderItem) {
    	   qr.update(conn, sql, item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
       }
	}

	//更新收货人信息
	public void updateOrderAdrr(Order order) throws SQLException {
        QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "update orders set address=?,name=?,telephone=? where oid=?";
        qr.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		
	}

	//支付成功，设置state值为1
	public void setState(String r6_Order) throws SQLException {
       QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
       String sql = "update orders set state=? where oid=?";
       qr.update(sql,1,r6_Order);
		
	}

	//获取所有订单
	public List<Order> findAllOrders(String uid) throws SQLException {
        QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "select * from orders where uid=?";
        return qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
		
	}

	// 获取所有的OrderItem及product中的数据
	public List<Map<String, Object>> findAllOrderItemByOid(String oid) throws SQLException {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select p.pimage,p.pname,p.shop_price,i.count,i.subtotal from orderItem i,product p where i.pid = p.pid and oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), oid);
		return mapList;
	}

	

}
