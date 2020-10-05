package com.azry.sps.console.server.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter("*.js")
public class JSCacheControlFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();

		if (requestURI.contains("nocache.js")) {
			Date now = new Date();
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setDateHeader("Date", now.getTime());
			// one day old
			httpResponse.setDateHeader("Expires", now.getTime() - 86400000L);
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("X-imposedNoCache", "true");
			httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		}

		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

	@Override
	public void destroy() {}
}

