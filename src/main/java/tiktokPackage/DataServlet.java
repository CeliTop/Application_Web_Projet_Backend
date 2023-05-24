package tiktokPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
@WebServlet("/DataServlet")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 10, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private BackendInterfaceLocal facade;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataServlet() {
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
		
		// Obtenir le compte si l'utilisateur est connecté
		Cookie LoginIDCookie = ServletUtils.getLoginIDCookie(request.getCookies());
		Compte compte = ServletUtils.getCompteFromCookie(LoginIDCookie, facade);

		String op = request.getParameter("op");
		if (op == null) {
			responseMap.put("message", "Parametre manquant");
			response.setStatus(400);
		} else if (op.equals("getRandomVideo")) {
			Video video = facade.getRandomVIdeo();
			if (compte!=null) {
				video.setLiked(facade.liked(compte.getId(), video.getId()));
				video.getCompteUploader().setAbonne(facade.estAbonne(compte.getId(), video.getCompteUploader().getId()));
			}
			responseMap.put("video", video);
		} else if (op.equals("getVideoInfos")) {
			int videoId = Integer.parseInt(request.getParameter("id"));
			Video video = facade.getVideoFromID(videoId);
			if (video == null) {
				response.getWriter().println("La vidéo n'a pas été trouvé");
				return;
			}
			if (compte!=null) {
				video.setLiked(facade.liked(compte.getId(), video.getId()));
				video.getCompteUploader().setAbonne(facade.estAbonne(compte.getId(), video.getCompteUploader().getId()));
			}
			responseMap.put("message", video);
		} else if (op.equals("getHashtagVideos")) {
			String hashtag = request.getParameter("hashtag");
			if (hashtag == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				Collection<Video> videos = facade.getHashtagVideos(hashtag);
				if (compte!=null) {
					for (Video video: videos) {
						video.setLiked(facade.liked(compte.getId(), video.getId()));
						video.getCompteUploader().setAbonne(facade.estAbonne(compte.getId(), video.getCompteUploader().getId()));
					}
				}
				responseMap.put("videos", videos);
			}
		} else if (op.equals("getLieuVideos")) {
			String lieu = request.getParameter("lieu");
			if (lieu == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				Collection<Video> videos = facade.getVideoFromLieu(lieu);
				if (compte!=null) {
					for (Video video: videos) {
						video.setLiked(facade.liked(compte.getId(), video.getId()));
						video.getCompteUploader().setAbonne(facade.estAbonne(compte.getId(), video.getCompteUploader().getId()));
					}
				}
				responseMap.put("videos", videos);
			}
		} else if (op.equals("getCompteVideos")) {
			String compteID = request.getParameter("compteID");
			if (compteID == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				Collection<Video> videos = facade.getVideosFromCompte(Integer.parseInt(compteID));
				if (compte!=null) {
					for (Video video: videos) {
						video.setLiked(facade.liked(compte.getId(), video.getId()));
						video.getCompteUploader().setAbonne(facade.estAbonne(compte.getId(), video.getCompteUploader().getId()));
					}
				}
				responseMap.put("videos", videos);
			}
		} else if (op.equals("getVideo")) {
			int videoId = Integer.parseInt(request.getParameter("id"));
			Video video = facade.getVideoFromID(videoId);
			if (video == null) {
				response.getWriter().println("La vidéo n'a pas été trouvé");
				return;
			}
			facade.addVue(video);
			String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "uploads" + File.separator
					+ video.getId();
			File file = new File(filePath);

			// Send the file
			if (file.exists()) {
				response.setContentType("video/mp4");
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "inline; filename=\"" + video.getId() + ".mp4\"");

				FileInputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = response.getOutputStream();

				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				return;
			}
		} else if (op.equals("getAllComptes")) {
			Collection<Compte> comptes = facade.getAllComptes();
			if (compte!=null) {
				for (Compte compteUploader: comptes) {
					compteUploader.setAbonne(facade.estAbonne(compte.getId(), compteUploader.getId()));
				}
			}
			responseMap.put("comptes", comptes);
		} else if (op.equals("getCompte")) {
			int compteID = Integer.parseInt(request.getParameter("id"));
			Compte compteFound = facade.getCompte(compteID);
			if (compteFound==null) {
				responseMap.put("message", "Utilisateur non trouvé");
				response.setStatus(400);
			} else {
				if (compte!=null) {
					compteFound.setAbonne(facade.estAbonne(compte.getId(), compteFound.getId()));
				}
				responseMap.put("compte", compteFound);
			}
		} else if (op.equals("likeVideo")) {
			if (compte == null) {
				response.setStatus(403);
				responseMap.put("message", "Utilisateur non connecté");
			} else {
				int videoID = Integer.parseInt(request.getParameter("videoID"));
				facade.likeVideo(compte.getId(), videoID);
			}
		} else if (op.equals("unlikeVideo")) {
			if (compte == null) {
				response.setStatus(403);
				responseMap.put("message", "Utilisateur non connecté");
			} else {
				int videoID = Integer.parseInt(request.getParameter("videoID"));
				facade.unlikeVideo(compte.getId(), videoID);
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

		String op = request.getParameter("op");
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		
		// Obtenir le compte si l'utilisateur est connecté
		Cookie LoginIDCookie = ServletUtils.getLoginIDCookie(request.getCookies());
		Compte compte = ServletUtils.getCompteFromCookie(LoginIDCookie, facade);

			if (op == null) {
			responseMap.put("message", "Parametre manquant");
			response.setStatus(400);
		} else if (op.equals("upload")) {
			if (compte == null) {
				response.setStatus(403);
				responseMap.put("message", "Utilisateur non connecté");
			} else {
				Video video = new Video();
				// Récupérer les hashtags sous la forme "&hashtags=Mood,Summer,Vibes"
				Collection<Hashtag> hashtags = new ArrayList<Hashtag>();
				String hashtagStrings = request.getParameter("hashtags"); 
				if (hashtagStrings != null) {
					for (String hashtagString: hashtagStrings.split(",")) {
						hashtags.add(new Hashtag(hashtagString));
					}
				}
				// Récupérer la description et localisation
				String desc = request.getParameter("description"); 
				video.setDescription(desc);
				String lieu = request.getParameter("lieu"); 
				video.setLieu(lieu);
				video = facade.posterVideo(compte, video, hashtags);
				String filename = Integer.toString(video.getId());
				Part filePart = request.getPart("file");
				if (filePart == null) {
					response.getWriter().println("Pas de vidéo à uploader");
					return;
				}
				Path parentPath = Paths.get(System.getProperty("jboss.server.data.dir") + "/uploads");
				if (!Files.exists(parentPath))
					Files.createDirectories(parentPath);
				String filePath = parentPath + File.separator + filename;
				InputStream fileContent = filePart.getInputStream();
				Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
				responseMap.put("message", video);
				responseMap.put("details", "fichier ajouté à l'adresse: " + filePath);
			}
		} else if (op.equals("addCommentaire")) {
			String text = request.getParameter("text");
			String videoID = request.getParameter("videoID");
			if (text == null || videoID == null) {
				responseMap.put("message", "Parametre manquant");
				response.setStatus(400);
			} else {
				if (compte == null) {
					response.setStatus(403);
					responseMap.put("message", "Utilisateur non connecté");
				} else {
					Commentaire commentaire = facade.addCommentaire(text, compte.getId(), Integer.parseInt(videoID));
					if (commentaire == null) {
						response.setStatus(400);
						responseMap.put("message", "Le commentaire n'as pas été uploadé");
					} else {
						responseMap.put("message", commentaire);
					}
				}
			} 
		} else if (op.equals("addAbonnement")) {
			String abonnementID = request.getParameter("abonnementID");
			if (facade.addAbonnement(compte.getId(), Integer.parseInt(abonnementID))) {
				responseMap.put("message", "Abonnement ajouté !");
			} else {
				responseMap.put("message", "Problème lors de l'abonnement");
			}
		} else if (op.equals("removeAbonnement")) {
			String abonnementID = request.getParameter("abonnementID");
			if (facade.removeAbonnement(compte.getId(), Integer.parseInt(abonnementID))) {
				responseMap.put("message", "Abonnement enlevé !");
			} else {
				responseMap.put("message", "Problème lors du désabonnement");
			}
		}
		String responseJson = gson.toJson(responseMap);
		response.getWriter().println(responseJson);
	}
}
