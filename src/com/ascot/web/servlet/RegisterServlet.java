package com.ascot.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.ascot.domain.User;
import com.ascot.service.UserService;
import com.ascot.utils.CommonUtils;
import com.ascot.utils.MailUtils;


public class RegisterServlet extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置编码为UTF-8
		request.setCharacterEncoding("utf-8");
		//获取注册界面的数据
		Map<String, String[]> properties = request.getParameterMap();
		User user = new User();
		try {
			//自己制定一个类型转换器(将String转为Date),在映射时，当找到Date.class类型的数据才会执行下面代码----BeanUtils包内的对象
			ConvertUtils.register(new Converter() {

				@Override
				public Object convert(Class clazz, Object values) {
					// 将String转为Date类型
					SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
					Date parse = null;
					try {
						parse = format.parse(values.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
				
			}, Date.class);
			//映射封装
			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		//封装注册界面中没有的属性
		//private String uid;
		user.setUid(CommonUtils.getUUID());
		//private String telephone;
		user.setTelephone("15875006036");
		//private int state;
		user.setState(0);
        //private String code;激活码
		String activeCode = CommonUtils.getUUID();
		user.setCode(activeCode);
		//将user传递给service层
		UserService service = new UserService();
		Boolean isRegisterSuccess= false;
		try {
			isRegisterSuccess = service.register(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//是否注册成功
		if(isRegisterSuccess) {
			//发送激活邮件
		    String emailMsg ="恭喜您注册成功，请点击下面的连接进行激活账号"+"<a href='http://localhost:8080/ascotShop/active?activeCode="+activeCode+"'>"
		    		+"http://localhost:8080/ascotShop/active?activeCode="+activeCode+"</a>";//设置一个激活servlet：active
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			//跳转到注册成功页面,重定向
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else {
			//跳转到注册失败页面
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
