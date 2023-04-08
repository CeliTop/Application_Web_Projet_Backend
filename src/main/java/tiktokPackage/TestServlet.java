package tiktokPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, 
    maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private BackendInterfaceLocal facade;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		if (op != null && op.equals("getVideo")) {
			// TODO replace sample video
			File file = new File("/tmp/tiktokfiles/sampleVideo.mp4");
	        response.setContentType("video/mp4");
	        response.setContentLength((int) file.length());
	        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
	        FileInputStream inputStream = new FileInputStream(file);
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            response.getOutputStream().write(buffer, 0, bytesRead);
	        }
	        inputStream.close();
		} else {	
			HashMap<String, Object> responseMap = new HashMap<String, Object>();
			Collection<Compte> comptes = facade.getAllComptes();
			responseMap.put("comptes", comptes);
			
			GsonBuilder builder = new GsonBuilder(); 
			builder.setPrettyPrinting(); 
			Gson gson = builder.create();
			String responseJson = gson.toJson(responseMap);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(responseJson);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		if (op.equals("addCompte")) {
			String name = request.getParameter("name");
			facade.addCompte(new Compte(name));
			
			GsonBuilder builder = new GsonBuilder(); 
			builder.setPrettyPrinting(); 
			Gson gson = builder.create();
			HashMap<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("message", name + " ajouté");
			String responseJson = gson.toJson(responseMap);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(responseJson);
		}
		else if (op.equals("upload")) {
			Part filePart = request.getPart("file");
			String filename = filePart.getSubmittedFileName();
			Path parentPath =  Paths.get(getServletContext().getRealPath("/uploads"));
			if (!Files.exists(parentPath))
			    Files.createDirectories(parentPath);
			String filePath = getServletContext().getRealPath("/uploads") + File.separator + filename;
			InputStream fileContent = filePart.getInputStream();
		    Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		    response.getWriter().println("fichier ajouté:" + getServletContext().getRealPath("/uploads"));
		}
	}

}
