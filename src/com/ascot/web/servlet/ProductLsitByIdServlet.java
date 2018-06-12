package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.domain.Category;
import com.ascot.domain.PageBean;
import com.ascot.domain.Product;
import com.ascot.service.ProductService;

public class ProductLsitByIdServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    ProductService service = new ProductService();
		//获取Cid
		String cid = request.getParameter("cid");
		//处理再次访问时获取当前页的页数
		 String currentPagestr = request.getParameter("currentPage");
		    if(currentPagestr==null) currentPagestr = "1";
		    int currentPage = Integer.parseInt(currentPagestr);
		    int pageSize = 12;
		//获取类别名
		Category category = null;
		try {
			category = service.findProductCategory(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//查询该cid对应所有商品并封装到PageBean中
	    PageBean pageBean = service.findProductListByCid(cid,currentPage,pageSize);
	    
	    //定义一个记录历史商品信息的集合
	    List<Product> historyProductList = new ArrayList<Product>();
	    
	    //获取request中cookie值
	    Cookie[] cookies = request.getCookies();
	    //如果存在，则进行存储
	    if(cookies!=null) {
	    	for(Cookie cookie : cookies) {
	    			if("pids".equals(cookie.getName())) {
	    				String pids = cookie.getValue();
	    				String[] splits = pids.split("-");
	    				for(String pid : splits) {
	    				    //根据pid查找商品信息
	    					Product pro = service.getProductByPid(pid);
	    				    historyProductList.add(pro);
	    				}
	    			}
	    	}
	    }
	    //将分页数据封装到request域中
	    request.setAttribute("pageBean", pageBean);
	    request.setAttribute("category", category);
	    request.setAttribute("cid", cid);
	    request.setAttribute("historyProductList",historyProductList);
	    //将request转发到jsp界面
	    request.getRequestDispatcher("/product_list.jsp").forward(request, response);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
