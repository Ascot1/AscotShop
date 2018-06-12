package com.ascot.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.domain.Category;
import com.ascot.domain.PageBean;
import com.ascot.domain.Product;
import com.ascot.service.CategoryService;
import com.ascot.service.ProductService;
import com.google.gson.Gson;


public class ProductServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	//获取首页信息
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//获取热门商品
    	ProductService service =  new ProductService();
    	List<Product>hotProduct = null;
    	List<Product>newProduct = null;
    	try {
    		hotProduct =  service.findHotProduct();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}	
        //获取最新商品
    	try {
    		newProduct =  service.findNewProduct();
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}	
    	
    	//将商品放于request域中
    	request.setAttribute("hotProduct", hotProduct);
    	request.setAttribute("newProduct", newProduct);
    	//将商品信息发送到页面
    	request.getRequestDispatcher("/index.jsp").forward(request, response);
    	
	}
    
    //商品详细信息展示
    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//获取request中pid
    			String pid = request.getParameter("pid");
    			String currentPage = request.getParameter("currentPage");
    			String cid = request.getParameter("cid");
    			//获取商品数据
    			ProductService service = new ProductService();
    		    Product product = service.getProductByPid(pid);
    		    
    		    //获得客户端携带cookie
    		    String pids = pid;
    		    //判断cookie中是否有值
    		    Cookie[] cookies = request.getCookies();
    		    if(cookies!=null) {
    		    	for(Cookie cookie:cookies) {
    		    		if("pids".equals(cookie.getName())) {
    		    			 pids = cookie.getValue();
    		    			//将pids拆成一个数组
    		    			String[] splits = pids.split("-");
    		    			List<String> asList =  Arrays.asList(splits);
    		    			//转化为链表形式3-2-1
    		    			LinkedList<String> list = new LinkedList<String>(asList);
    		    			//如果链表中包含该商品的pid，怎将其删除之后置于链表首部；否则将其置于链表首部
    		    			if(list.contains(pid)) {
    		    				list.remove(pid);
    		    				list.addFirst(pid);
    		    			}else {
    		    				list.addFirst(pid);
    		    			}
    		    			//将{3，2，1}转化为3-2-1
    		    			StringBuffer sb = new StringBuffer();
    		    			for(int i = 0;i<list.size();i++) {
    		    				sb.append(list.get(i));
    		    				sb.append("-");
    		    				
    		    			}
    		    			pids = sb.substring(0,sb.length()-1);
    		    		}
    		    	}
    		    }
    		    //加了else为什么会不执行下面这段代码
    		    	Cookie cookie_pids = new Cookie("pids",pids);
    		    	response.addCookie(cookie_pids);
    		    //封装到request域中
    		    request.setAttribute("product", product);
    		    request.setAttribute("currentPage", currentPage);
    		    request.setAttribute("cid",cid);
    		    //将数据传递到product_info.jsp中,重定向
    		    request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    
    }
    //通过cid获取商品列表
    public void productListByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    
    //获取商品类别
    public void category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

}
