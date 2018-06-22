package com.ascot.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ascot.domain.User;

/**
 * 该过滤器主要用于测试，并没有在程序中起到作用
 * @author cldn1
 *
 */
public class UserLoginPrivilegeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest res = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = res.getSession();
		User user = (User) session.getAttribute("user");
		// 判断是否登陆,如果没登录则跳转到登陆界面
		if (user == null) {
			resp.sendRedirect(res.getContextPath() + "/login.jsp");
			return;
		}
		
		chain.doFilter(res, resp);
	}

	@Override
	public void destroy() {

	}

}
