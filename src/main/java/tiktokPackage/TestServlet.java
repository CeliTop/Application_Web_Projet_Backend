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
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation(); 
		builder.setPrettyPrinting(); 
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
	    
	    String op = request.getParameter("op");
		if (op != null && op.equals("getVideo")) {
			// TODO replace sample video
			//File file = new File("/tmp/tiktokfiles/0");
			Video video = facade.getRandomVIdeo();
			responseMap.put("video", video);
			
		} else {	
			Collection<Compte> comptes = facade.getAllComptes();
			responseMap.put("comptes", comptes);
		}
		String responseJson = gson.toJson(responseMap);
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(responseJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
	    response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
	    response.addHeader("Access-Control-Max-Age", "1728000");
	    
		String op = request.getParameter("op");
		System.out.println(op);
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation(); 
		builder.setPrettyPrinting(); 
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		
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
			responseMap.put("message", name + " ajouté");
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
			Compte compte = facade.getCompte(1);
			video = facade.posterVideo(compte, video);
			String filename = Integer.toString(video.getId());
			System.out.println("JESUISLA");
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
		    //TODO poster video compte - mettre vérification compte au début
		    responseMap.put("message", "fichier ajouté: " + getServletContext().getRealPath("/uploads") + "/" + filename + " ajouté");
		}
		String responseJson = gson.toJson(responseMap);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(responseJson);
	}

}
