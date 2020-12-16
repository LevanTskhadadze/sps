package com.azry.sps.console.server;

import com.azry.sps.console.server.file.FileManager;
import com.azry.sps.server.services.service.ServiceManager;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/sps/servlet/iconDownload")
public class IconDownloadServlet extends HttpServlet {

	@Inject
	FileManager fileManager;

	@Inject
	ServiceManager serviceManager;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		String source = serviceManager.getIcon(id);

		byte[] buffer;
		try {
			buffer = fileManager.downloadIcon(source);
		} catch (Exception ex) {
			buffer = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("/images/empty_icon.gif"));

		}
		resp.setContentType("image/gif");

		OutputStream outputStream = resp.getOutputStream();
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
	}
}