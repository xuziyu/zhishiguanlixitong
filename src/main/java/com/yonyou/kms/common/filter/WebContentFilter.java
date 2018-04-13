package com.yonyou.kms.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * 支持跨域 接口访问过滤器 提供业务员app访问知识库接口支持
 * Servlet Filter implementation class WebContentFilter
 */
@Component
public class WebContentFilter implements Filter {

    /**
     * Default constructor. 
     */
    public WebContentFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		//httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		//httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authentication");
		
		
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*"); 
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"); 
		httpServletResponse.setHeader("Access-Control-Max-Age", "3600"); 
		httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,Content-Type,Access-Token"); 
		httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
