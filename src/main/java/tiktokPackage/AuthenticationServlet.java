package tiktokPackage;

import java.io.IOException;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class AuthenticationServlet
 */
@WebServlet("/AuthenticationServlet")
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB
	private BackendInterfaceLocal facade;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticationServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
	    GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation(); 
		builder.setPrettyPrinting(); 
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		
		String op = request.getParameter("op");
		if (op.equals("login")) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			if (name == null || password == null) {
				response.setStatus(400);
				response.getWriter().println("requete incomplete");
				return;
			}
			Compte dbCompte = facade.login(new Compte(name, password));
			if (dbCompte != null) {
				//Cookie IDCookie = new Cookie("loginID", Integer.toString(dbCompte.getId()));
				//response.addCookie(IDCookie);
				response.setHeader("Set-Cookie", String.format("loginID=%d; SameSite=None; Secure", dbCompte.getId()));
				responseMap.put("message", name + " connecté !");
			} else {
				responseMap.put("message", "L'utilisateur n'existe pas ou le mot de passe est erroné");
				response.setStatus(403);
			}
		}
		
		String responseJson = gson.toJson(responseMap);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(responseJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

	    GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation(); 
		builder.setPrettyPrinting(); 
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		
		String op = request.getParameter("op");
		if (op.equals("createCompte")) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			if (name == null || password == null) {
				response.setStatus(400);
				response.getWriter().println("requete incomplete");
				return;
			}
			boolean correct = facade.addCompte(new Compte(name, password));
			if (correct) {
				responseMap.put("message", name + " ajouté");
			} else {
				responseMap.put("message", "Le nom " + name + " est déja utilisé");
				response.setStatus(422);
			}
		}
		
		String responseJson = gson.toJson(responseMap);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(responseJson);
	}

}
