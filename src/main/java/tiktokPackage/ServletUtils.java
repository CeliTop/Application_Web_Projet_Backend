package tiktokPackage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {
	/**
	 * Setup all the headers and infos of the response (CORS, Content-Type, Character-Encoding...)
	 * @param request
	 * @param response
	 */
	static void SetupHeader(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	}
	
	static Cookie getLoginIDCookie(Cookie[] cookies) {
		Cookie loginIDCookie = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("loginID")) {
					loginIDCookie = cookie;
					break;
				}
			}
		}
		return loginIDCookie;
	}
	
	static Compte getCompteFromCookie(Cookie loginCookie, BackendInterfaceLocal facade) {
		if (loginCookie == null) {return null;}
		else {
			String id = loginCookie.getValue();
			Compte compte = facade.getCompte(Integer.parseInt(id));
			if (compte == null) {return null;}
			return compte;
		}
	}
}
