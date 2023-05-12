package tiktokPackage;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

@Singleton
public class TikTokEJBSess implements BackendInterfaceLocal, BackendInterfaceRemote{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public int addCompte(Compte c) {
		try {	
			em.persist(c);
			return c.getId();
		} catch (PersistenceException e) {
			return 0;
		}
	}
	
	@Override
	public Compte getCompte(int id) {
		Compte compte = em.find(Compte.class, id);
		return compte;
	}

	@Override
	public Collection<Compte> getAllComptes() {
		TypedQuery<Compte> req = em.createQuery("SELECT c FROM Compte c",
				Compte.class);
		return req.getResultList();
	}

	@Override
	public Video posterVideo(Compte c, Video video, Collection<Hashtag> hashtags) {
		em.persist(video);
		Compte cdatabase = em.find(Compte.class, c.getId());
		video.setCompteUploader(cdatabase);
		// Ajouter les hashtags
		for (Hashtag hashtag: hashtags) {
			Hashtag bddHash = em.find(Hashtag.class, hashtag.getHashtagName());
			if (bddHash==null) {	
				em.persist(hashtag);
				video.addHashtag(hashtag);
			} else {
				//Hashtag bddHash = results.get(0);
				video.addHashtag(bddHash);
			}
		}
		return video;
	}
	
	public Video getRandomVIdeo() {
		TypedQuery<Video> req = em.createQuery("SELECT v FROM Video v ORDER BY RAND()",Video.class).setMaxResults(1);
		return req.getResultList().get(0);
	}
	
	public Video getVideoFromID(int id) {
		Video video = em.find(Video.class, id);
		return video;
	}

	@Override
	public Compte login(Compte c) {
		TypedQuery<Compte> req = em.createQuery("SELECT c FROM Compte c WHERE nom=?1",
				Compte.class);
		req.setParameter(1, c.getNom());
		List<Compte>  comptes = req.getResultList();
		if (comptes.size() == 0) return null;
		Compte dbCompte = comptes.get(0);
		if (dbCompte == null || !(dbCompte.getPassword().equals(c.getPassword()))) return null;
		return dbCompte;
	}
	
	@Override
	public Commentaire addCommentaire(String commentaireText, int compteID, int videoID) {
		Compte c = getCompte(compteID);
		Video v = getVideoFromID(videoID);
		if (c==null || v==null) {return null;}
		Commentaire commentaire = new Commentaire(commentaireText, c, 0, new Date());
		em.persist(commentaire);
		v.addCommentaire(commentaire);
		return commentaire;
	}

	@Override
	public void likeVideo(int compteID, int videoID) {
		Compte c = getCompte(compteID);
		Video v = getVideoFromID(videoID);
		if (!c.getVideosLike().contains(v)) {
			v.like();
			c.addVideoLike(v);
		}
		em.merge(v);
		em.merge(c);
	}

	@Override
	public void unlikeVideo(int compteID, int videoID) {
		Compte c = getCompte(compteID);
		Video v = getVideoFromID(videoID);
		if (c.getVideosLike().contains(v)) {
			v.unlike();
			c.removeVideoLike(v);
		}
		em.merge(v);
		em.merge(c);
	}
	
	@Override
	public Collection<Integer> getHashtagVideos(String hashtag){
		Hashtag hash = em.find(Hashtag.class, hashtag);
		if (hash==null) {return null;}
		return hash.getVideos().stream().map(v -> v.getId()).collect(Collectors.toList());
	}
}
