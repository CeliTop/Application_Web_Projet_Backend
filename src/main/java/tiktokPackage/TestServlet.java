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
import javax.servlet.http.Cookie;
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
		if (op == null) {
			response.getWriter().println("Pas d'action demandée en POST");
			return;
		}
		if (op.equals("addCompte")) {
			String name = request.getParameter("name");
			if (name == null) {
				response.getWriter().println("Un compte nécessite un nom");
				return;
			}
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
			// TODO verifier que l'utilisateur est connecté à un compte
//	        Cookie CompteIDCookie = null;
//	        Cookie[] cookies = request.getCookies();
//	        if (cookies != null) {
//	            for (Cookie cookie : cookies) {
//	                if (cookie.getName().equals("id")) {
//	                	CompteIDCookie = cookie;
//	                    break;
//	                }
//	            }
//	        }
//	        if (CompteIDCookie == null) {
//	        	response.getWriter().println("L'utilisateur n'est pas connecté");
//	        	return;
//	        }
//            String id = CompteIDCookie.getValue();
//            Compte compte = facade.getCompte(Integer.parseInt(id));
//            if (compte == null) {
//            	response.getWriter().println("ID non reconnu");
//            	return;
//            }

			Video video = new Video();
			String filename = Integer.toString(video.getId());
			Part filePart = request.getPart("file");
			if (filePart == null) {
				response.getWriter().println("Pas de vidéo à uploader");
				return;
			}

			Path parentPath =  Paths.get(getServletContext().getRealPath("/uploads"));
			if (!Files.exists(parentPath))
			    Files.createDirectories(parentPath);
			String filePath = getServletContext().getRealPath("/uploads") + File.separator + filename;
			InputStream fileContent = filePart.getInputStream();
		    Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		    video.setFilePath(filePath);
		    //TODO poster video compte - mettre vérification compte au début
		    //facade.posterVideo(compte, video);

		    response.getWriter().println("fichier ajouté: " + getServletContext().getRealPath("/uploads"));
		}
	}

}
