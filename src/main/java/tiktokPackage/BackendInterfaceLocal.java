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
	 * Post a video to the database
	 * @param c the account to which the video will be linked
	 * @param video the video itself
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
	 * @param v la video qui est commenté
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
}
