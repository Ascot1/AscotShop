package com.ascot.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.ascot.domain.Cart;
import com.ascot.domain.CartItem;
import com.ascot.domain.Category;
import com.ascot.domain.Order;
import com.ascot.domain.OrderItem;
import com.ascot.domain.PageBean;
import com.ascot.domain.Product;
import com.ascot.domain.User;
import com.ascot.service.CategoryService;
import com.ascot.service.ProductService;
import com.ascot.utils.CommonUtils;
import com.ascot.utils.PaymentUtil;
import com.google.gson.Gson;

//直接调用父类的service方法
public class ProductServlet extends BaseServlet {

	// 获取首页信息
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取热门商品
		ProductService service = new ProductService();
		List<Product> hotProduct = null;
		List<Product> newProduct = null;
		try {
			hotProduct = service.findHotProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 获取最新商品
		try {
			newProduct = service.findNewProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 将商品放于request域中
		request.setAttribute("hotProduct", hotProduct);
		request.setAttribute("newProduct", newProduct);
		// 将商品信息发送到页面
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	// 商品详细信息展示
	public void productInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取request中pid
		String pid = request.getParameter("pid");
		String currentPage = request.getParameter("currentPage");
		String cid = request.getParameter("cid");
		// 获取商品数据
		ProductService service = new ProductService();
		Product product = service.getProductByPid(pid);

		// 获得客户端携带cookie
		String pids = pid;
		// 判断cookie中是否有值
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids = cookie.getValue();
					// 将pids拆成一个数组
					String[] splits = pids.split("-");
					List<String> asList = Arrays.asList(splits);
					// 转化为链表形式3-2-1
					LinkedList<String> list = new LinkedList<String>(asList);
					// 如果链表中包含该商品的pid，怎将其删除之后置于链表首部；否则将其置于链表首部
					if (list.contains(pid)) {
						list.remove(pid);
						list.addFirst(pid);
					} else {
						list.addFirst(pid);
					}
					// 将{3，2，1}转化为3-2-1
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < list.size(); i++) {
						sb.append(list.get(i));
						sb.append("-");

					}
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}
		// 加了else为什么会不执行下面这段代码
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);
		// 封装到request域中
		request.setAttribute("product", product);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("cid", cid);
		// 将数据传递到product_info.jsp中,重定向
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);

	}

	// 通过cid获取商品列表
	public void productListByCid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service = new ProductService();
		// 获取Cid
		String cid = request.getParameter("cid");
		// 处理再次访问时获取当前页的页数
		String currentPagestr = request.getParameter("currentPage");
		if (currentPagestr == null)
			currentPagestr = "1";
		int currentPage = Integer.parseInt(currentPagestr);
		int pageSize = 12;
		// 获取类别名
		Category category = null;
		try {
			category = service.findProductCategory(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 查询该cid对应所有商品并封装到PageBean中
		PageBean pageBean = service.findProductListByCid(cid, currentPage, pageSize);

		// 定义一个记录历史商品信息的集合
		List<Product> historyProductList = new ArrayList<Product>();

		// 获取request中cookie值
		Cookie[] cookies = request.getCookies();
		// 如果存在，则进行存储
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					String pids = cookie.getValue();
					String[] splits = pids.split("-");
					for (String pid : splits) {
						// 根据pid查找商品信息
						Product pro = service.getProductByPid(pid);
						historyProductList.add(pro);
					}
				}
			}
		}
		// 将分页数据封装到request域中
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("category", category);
		request.setAttribute("cid", cid);
		request.setAttribute("historyProductList", historyProductList);
		// 将request转发到jsp界面
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);

	}

	// 获取商品类别
	public void category(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取Category数据
		CategoryService service = new CategoryService();
		List<Category> categoryList = null;
		try {
			categoryList = service.findAllCategory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 将集合封装为json
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		response.setCharacterEncoding("utf-8");
		// 将json返回页面
		response.getWriter().write(json);
	}

	// 添加商品到购物车
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		// 获取购买数量，传过来的数据都是String类型的，所以需要转换
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		double subtotal = 0.0;
		int oldBuyNum = 0;
		int newBuyNum = 0;
		double oldSubtotal = 0.0;
		double newSubtotal = 0.0;
		double total = 0.0;

		// 将商品添加到购物项
		ProductService service = new ProductService();
		Product product = service.getProductByPid(pid);
		CartItem item = new CartItem();
		item.setProduct(product);

		// 添加购买数目
		item.setBuyNum(buyNum);

		// 添加小计
		subtotal = buyNum * product.getShop_price();
		item.setSubtotal(subtotal);

		// 获取session
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");

		// 判断session中是否有购物车
		if (cart == null) {
			// 创建购物车
			cart = new Cart();
		}
		// 获取cart中的cartItem
		Map<String, CartItem> cartItem = cart.getCartItem();
		// 判断session中是否存在该购物项
		if (cartItem.containsKey(pid)) {
			oldBuyNum = cartItem.get(pid).getBuyNum();
			newBuyNum = oldBuyNum + buyNum;
			item.setBuyNum(newBuyNum);
			oldSubtotal = cartItem.get(pid).getSubtotal();
			newSubtotal = oldSubtotal + subtotal;
			item.setSubtotal(newSubtotal);
		}
		// 如果session中有购物车，则将购物项添加到购物车
		cart.getCartItem().put(product.getPid(), item);
		total = cart.getTotal() + item.getSubtotal();
		cart.setTotal(total);
		session.setAttribute("cart", cart);
		// 直接跳转到购物车页面
		response.sendRedirect(request.getContextPath() + "/cart.jsp");
	}

	public void delProductFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取所删除商品pid
		String pid = request.getParameter("pid");

		// 删除session中购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");

		if (cart != null) {
			Map<String, CartItem> cartItem = cart.getCartItem();
			cart.setTotal(cart.getTotal() - cartItem.get(pid).getSubtotal());
			cartItem.remove(pid);
			cart.setCartItem(cartItem);
		}
		session.setAttribute("cart", cart);

		// 跳转回到cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}

	// 清空购物车
	public void cleanCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 删除session中的购物车中购物项集合中的所有item
		HttpSession session = request.getSession();
		session.removeAttribute("cart");

		// 跳转到cart.jsp
		response.sendRedirect(request.getContextPath() + "/cart.jsp");

	}

	// 提交订单
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Cart cart = (Cart) session.getAttribute("cart");
		// 判断是否登陆,如果没登录则跳转到登陆界面
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		// 封装order对象
		Order order = new Order();
		// 1. private String oid
		order.setOid(CommonUtils.getUUID());
		// 2.private Date orderTime
		order.setOrdertime(new Date());
		// 3.private double total
		order.setTotal(cart.getTotal());
		// 4.private int state
		order.setState(0);
		// 5. private String address
		order.setAddress(null);
		// 6. private String name
		order.setName(null);
		// 7.private String telephone
		order.setTelephone(null);
		// 8.private User user
		order.setUser(user);
		// 9. private List<OrderItem> orderItem(封装orderItem)
		Map<String, CartItem> cartItem = cart.getCartItem();
		for (Map.Entry<String, CartItem> entry : cartItem.entrySet()) {
			CartItem item = entry.getValue();
			OrderItem orderItem = new OrderItem();
			// 9.1.private String itemid
			orderItem.setItemid(CommonUtils.getUUID());
			// 9.2.private int count
			orderItem.setCount(item.getBuyNum());
			// 9.3.private double subtotal
			orderItem.setSubtotal(item.getSubtotal());
			// 9.4.private Product product
			orderItem.setProduct(item.getProduct());
			// 9.5.private Order order
			orderItem.setOrder(order);
			order.getOrderItem().add(orderItem);
		}
		// 10.将封装的数据传到service层,存储到数据库中
		ProductService service = new ProductService();
		service.submitOrder(order);
		// 11.将order放入session中
		session.setAttribute("order", order);
		// 12.跳转到order_info页面
		response.sendRedirect(request.getContextPath() + "/order_info.jsp");

	}

	// 更新收货人信息并进行在线支付
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.更新收货人信息
		Map<String, String[]> properties = request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, properties);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		ProductService service = new ProductService();
		service.updateOrderAdrr(order);

		// 2.在线支付
		// 获得 支付必须基本数据
		String orderid = request.getParameter("oid");
		// String money = order.getTotal()+"";正式版使用
		String money = "0.01";
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId=" + pd_FrpId + "&p0_Cmd=" + p0_Cmd
				+ "&p1_MerId=" + p1_MerId + "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur=" + p4_Cur
				+ "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat + "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url
				+ "&p9_SAF=" + p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse=" + pr_NeedResponse + "&hmac=" + hmac;

		// 重定向到第三方支付平台
		response.sendRedirect(url);
	}

	// 获取我的订单
	public void myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		// 判断是否登陆,如果没登录则跳转到登陆界面
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		// 查询orders表
		ProductService service = new ProductService();
		List<Order> orderList = service.findAllOrders(user.getUid());
		// 循环所有的订单 为每个订单填充订单集合信息
		if (orderList != null) {
			for (Order order : orderList) {
				String oid = order.getOid();
				List<Map<String, Object>> mapList = service.findAllOrderItemByOid(oid);
				// 将map中的数据封装到orderItem中
				for (Map<String, Object> orderItem : mapList) {
					OrderItem item = new OrderItem();
					Product product = new Product();
					try {
						// 从map中取出count、subtotal封装到item中
						BeanUtils.populate(item, orderItem);
						// 从map中取出pimage、pname、shop_price封装到product
						BeanUtils.populate(product, orderItem);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					item.setProduct(product);
					// 封装到订单中
					order.getOrderItem().add(item);
				}
			}
		}
		// 跳转到order_list页面
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
	}

}
