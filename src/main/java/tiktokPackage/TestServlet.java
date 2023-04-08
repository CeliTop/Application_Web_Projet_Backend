package tiktokPackage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
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
		Collection<Compte> comptes = facade.getAllComptes();
		GsonBuilder builder = new GsonBuilder(); 
		builder.setPrettyPrinting(); 
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("comptes", comptes);
		String responseJson = gson.toJson(responseMap);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(responseJson);
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
	}

}
