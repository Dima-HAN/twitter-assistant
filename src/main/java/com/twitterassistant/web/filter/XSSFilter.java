package com.twitterassistant.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XSSFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {	
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//DO NOT ALLOW OUR PAGE TO BE EMBEDDED
		//TEMP DISABLED AS IT DOES NOT WORK IN CHROME EXT
		//httpResponse.setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");	
		
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
	}

}
