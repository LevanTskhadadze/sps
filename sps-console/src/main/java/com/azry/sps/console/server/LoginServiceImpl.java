package com.azry.sps.console.server;

import com.azry.sps.console.loginshared.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

@WebServlet("/login/servlet/login")
@Slf4j
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	@Override
	public boolean login(String username, String password) {
		HttpServletRequest req = getThreadLocalRequest();
		try {
			req.login(username, password);
		} catch (ServletException ex) {
			log.error("Login failed", ex);
			return false;
		}
		return true;
	}

}