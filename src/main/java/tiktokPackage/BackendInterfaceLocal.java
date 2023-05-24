package tiktokPackage;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface BackendInterfaceLocal {
	/**
	 * Add an account to the database 
	 * @param c the account that need to be added to the database
	 * @return true if the account is added to the database, false if an error occured
	 */
	public int addCompte(Compte c);
	
	/**
	 * Get an account from it's id
	 * @param id of the account that need to be accessed
	 * @return the account (null if not found)
	 */
	public Compte getCompte(int id);
	
	/**
	 * Get all the accounts of the database
	 * @return every accounts
	 */
	public Collection<Compte> getAllComptes();
	
	/**
	 * Get all the videos of an account
	 * @param compteId the account ID
	 * @return all the videos ID of the account
	 */
	public Collection<Video> getVideosFromCompte(int compteId);
	
	/**
	 * Post a video to the database
	 * @param c the account to which the video will be linked
	 * @param video the video itself
	 * @param hashtags the list of hashtags of the video
	 * @return the new video object created by the database (with extended infos)
	 */
	public Video posterVideo(Compte c, Video video, Collection<Hashtag> hashtags);
	
	/**
	 * get a random video object from the database
	 * @return a random video
	 */
	public Video getRandomVIdeo();
	
	/**
	 * Get the video object from a video id
	 * @param id the id of the video wanted
	 * @return the video object
	 */
	public Video getVideoFromID(int id);
	
	/**
	 * Find the account from a name-password couple
	 * @param c the pseudo-account provided
	 * @return the database account if a match is found, null otherwise
	 */
	public Compte login(Compte c);
	
	/**
	 * Ajouter un commentaire de la part du compte c sur la video v
	 * @param commentaireText le texte du commentaire
	 * @param compteID l'id du compte qui upload le commentaire
	 * @param videoID la video qui est commentée
	 * @return l'objet commentaire enregistré dans la bdd
	 */
	public Commentaire addCommentaire(String commentaireText, int compteID, int videoID);
	
	/**
	 * Ajoute un like à la vidéo.
	 * @param compteID l'id du compte qui like la vidéo
	 * @param videoID l'id de la vidéo like
	 */
	public void likeVideo(int compteID, int videoID);
	
	/**
	 * Enlève un like à la vidéo.
	 * @param compteID l'id du compte qui unlike la vidéo
	 * @param videoID l'id de la vidéo unlike
	 */
	public void unlikeVideo(int compteID, int videoID);
	
	/**
	 * Does an account has liked the video
	 * @param compteID
	 * @param videoID
	 * @return
	 */
	public boolean liked(int compteID, int videoID);
	
	/**
	 * Ajoute une vue à la vidéo
	 * @param vue la video vue
	 */
	public void addVue(Video vue);
	
	/**
	 * Find all the videos corresponding to a hashtag
	 * @param hashtag
	 * @return
	 */
	public Collection<Video> getHashtagVideos(String hashtag);
	
	/**
	 * Ajoute un abonnement
	 * @param compteID le compte qui s'abonne
	 * @param abonnementID le compte qui va gagner un abonné
	 * @return true si l'ajout est correct
	 */
	public boolean addAbonnement(int compteID, int abonnementID);
	
	/**
	 * Enleve un abonnement
	 * @param compteID le compte qui se desabonne
	 * @param abonnementID le compte qui va perdre un abonné
	 * @return true si le desabonnement est correct
	 */
	public boolean removeAbonnement(int compteID, int abonnementID);
	
	/**
	 * Savoir si on est abonné à un compte
	 * @param compteID
	 * @param abonnementID
	 * @return
	 */
	public boolean estAbonne(int compteID, int abonnementID);
	
	public Collection<Video> getVideoFromLieu(String lieu);
}
