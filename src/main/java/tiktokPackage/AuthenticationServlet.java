package tiktokPackage;

import java.io.IOException;
import java.util.Date;
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
				// Login using cookie and return the account
				System.out.println(request.getCookies());
				Cookie LoginIDCookie = ServletUtils.getLoginIDCookie(request.getCookies());
				Compte compte = ServletUtils.getCompteFromCookie(LoginIDCookie, facade);
				if (compte==null) {
					responseMap.put("message", "Parametre manquant ou cookie manquant");
					response.setStatus(400);
				} else {
					responseMap.put("compte", compte);
				}
			} else {
				// Login using password and return the account
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
		} else if (op.equals("logout")){
			response.setHeader("Set-Cookie","loginID=-1;SameSite=None;Secure;expires=Thu, 01 Jan 1970 00:00:00 GMT");
			responseMap.put("message", "Déconnection réussie");
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
			String pp = request.getParameter("pp");
			Date currentDate = new Date();
			// Robustesse
			if (name == null || password == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				Compte compte = facade.addCompte(new Compte(name, password, bio, currentDate, surnom, Integer.parseInt(pp)));
				if (compte != null) {
					responseMap.put("compte", compte);
					response.setHeader("Set-Cookie",
							String.format("loginID=%d; SameSite=None; Secure", compte.getId()));
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
