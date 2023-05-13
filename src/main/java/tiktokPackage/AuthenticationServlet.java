package tiktokPackage;

import java.io.IOException;
import java.util.Date;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtils.SetupHeader(request, response);

		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();

		String op = request.getParameter("op");
		// Robustesse
		if (op == null) {
			responseMap.put("message", "Parametre manquant");
			response.setStatus(400);
		} else if (op.equals("login")) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			// Robustesse
			if (name == null || password == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				Compte dbCompte = facade.login(new Compte(name, password));
				if (dbCompte == null) {
					responseMap.put("message", "L'utilisateur n'existe pas ou le mot de passe est erroné");
					response.setStatus(403);
				} else {
					response.setHeader("Set-Cookie",
							String.format("loginID=%d; SameSite=None; Secure", dbCompte.getId()));
					responseMap.put("compte", dbCompte);
				}
			}
		}
		String responseJson = gson.toJson(responseMap);
		response.getWriter().println(responseJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtils.SetupHeader(request, response);

		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();

		String op = request.getParameter("op");
		// Robustesse
		if (op == null) {
			responseMap.put("message", "Parametre manquant");
			response.setStatus(400);
		} else if (op.equals("createCompte")) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String bio = request.getParameter("bio");
			String surnom = request.getParameter("surnom");
			Date currentDate = new Date();
			// Robustesse
			if (name == null || password == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				int compteID = facade.addCompte(new Compte(name, password, bio, currentDate, surnom));
				if (compteID>0) {
					responseMap.put("message", name + " ajouté");
					response.setHeader("Set-Cookie",
							String.format("loginID=%d; SameSite=None; Secure", compteID));
				} else {
					responseMap.put("message", "Le nom " + name + " est déja utilisé");
					response.setStatus(422);
				}
			}
		}
		String responseJson = gson.toJson(responseMap);
		response.getWriter().println(responseJson);
	}

}
