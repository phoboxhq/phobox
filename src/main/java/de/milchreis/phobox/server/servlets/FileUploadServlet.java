package de.milchreis.phobox.server.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.PhoboxModel;

public class FileUploadServlet extends HttpServlet {
	private static Logger log = Logger.getLogger(FileUploadServlet.class);
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		response.setContentType("application/json");
		JSONObject jsonresp = new JSONObject();
		
		PhoboxModel model = Phobox.getModel();
		
		DiskFileItemFactory factory = new DiskFileItemFactory(30000000, model.getIncomingPath());
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			List<FileItem> items = upload.parseRequest(request);
			
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = iter.next();

			    if(!item.isFormField()) {
			    	item.write(new File(model.getIncomingPath(), item.getName()));
			    }
			}
			jsonresp.put("status", "OK");
		} catch (Exception e) {
			jsonresp.put("status", "ERROR");
			log.error("Error while uploading file", e);
		}

		PrintWriter out = response.getWriter();
		out.println(jsonresp.toString());
	}
}
