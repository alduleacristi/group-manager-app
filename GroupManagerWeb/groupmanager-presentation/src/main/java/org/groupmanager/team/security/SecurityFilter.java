package org.groupmanager.team.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SecurityFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("***** Se filtreaza requestul ******");
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String token = servletRequest.getHeader("token");
		System.out.println("***** token-ul primit de la client: "+token);

		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}
