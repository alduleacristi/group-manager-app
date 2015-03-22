package org.groupmanager.team.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.groupmanager.team.model.User;
import org.groupmanager.team.user.GroupManagerSession;

public class SecurityFilter implements Filter {
	@Inject
	private GroupManagerSession session;

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("***** Se filtreaza requestul ******");
		if (session != null)
			System.out.println("S-a injectat ");
		else
			System.out.println("Nu s-a injectat");

		HttpServletRequest servletRequest = (HttpServletRequest) request;

		String token = servletRequest.getHeader("Authorization");
		System.out.println("***** token-ul primit de la client: " + token);
		User user = session.getUserByKey(token);
		if (token == null || user == null) {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setStatus(401);
			resp.sendError(401, "You are not authorized to get users");
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {

	}

}
