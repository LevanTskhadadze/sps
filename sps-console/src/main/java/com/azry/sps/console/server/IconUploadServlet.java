package com.azry.sps.console.server;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.server.file.FileManager;
import com.azry.sps.server.services.service.ServiceManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/sps/servlet/iconUpload")
public class IconUploadServlet extends HttpServlet {

	private static final int MAX_FILE_UPLOAD_MB = 100;

	private static final Logger log = LoggerFactory.getLogger(IconUploadServlet.class);

	@Inject
	private FileManager fileManager;

	@Inject
	private ServiceManager serviceManager;

	private static List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
		"image/jpeg",
		"image/gif",
		"image/png"
	);

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");

			int maxFileSize = MAX_FILE_UPLOAD_MB * 1024 * 1024;

			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
			fileUpload.setSizeMax(maxFileSize);

			List<FileItem> items = fileUpload.parseRequest(req);

			for (Object object : items) {
				FileItem item = (FileItem) object;
				if (item.getSize() > maxFileSize) {
					//showResponse(resp, "ERROR", Mes.get("iconSizeLimitExceeded", String.valueOf(MAX_FILE_UPLOAD_MB)));
					return;
				}

				if (!ALLOWED_CONTENT_TYPES.contains(item.getContentType())) {
					//showResponse(resp, "ERROR", Mes.get("iconTypeNotSupported"));
					return;
				}

				String[] tokens = item.getFieldName().split("-");
				long id = Long.parseLong(tokens[0]);
				String source = tokens[1];

				byte[] buffer = IOUtils.toByteArray(item.getInputStream());
				String fileName = fileManager.uploadIcon(source, buffer);
				//showResponse(resp, "OK", "");
				serviceManager.setIcon(id, fileName);

				if (!item.isInMemory()) {
					item.delete();
				}
			}
		} catch (Exception ex) {
			//showResponse(resp, "ERROR", Mes.get("unexpectedError"));
			log.warn("Error occurred during uploading file", ex);
		}
	}

	private void showResponse(HttpServletResponse response, String status, String info) throws IOException {
		response.setContentType("text/html");
		response.getWriter().write(status);
		response.flushBuffer();
	}
}