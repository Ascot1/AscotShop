package com.ascot.web.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascot.domain.PageBean;
import com.ascot.domain.Product;
import com.ascot.service.ProductService;

/**
 * Servlet implementation class ProductInfoServlet
 */
public class ProductInfoServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
