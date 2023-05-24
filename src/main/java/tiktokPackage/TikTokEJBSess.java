package tiktokPackage;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
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
		compte.setNbAbonnes();
		compte.setNbVideos();
		return compte;
	}

	@Override
	public Collection<Compte> getAllComptes() {
		TypedQuery<Compte> req = em.createQuery("SELECT c FROM Compte c",
				Compte.class);
		Collection<Compte> comptes = req.getResultList();
		for (Compte compte: comptes) {
			compte.setNbAbonnes();
			compte.setNbVideos();
		}
		return comptes;
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
		video.getCompteUploader().setNbAbonnes();
		video.getCompteUploader().setNbVideos();
		return video;
	}
	
	public Video getRandomVIdeo() {
		TypedQuery<Video> req = em.createQuery("SELECT v FROM Video v ORDER BY RAND()",Video.class).setMaxResults(1);
		Video video = req.getResultList().get(0);
		video.getCompteUploader().setNbAbonnes();
		video.getCompteUploader().setNbVideos();
		return video;
	}
	
	public Video getVideoFromID(int id) {
		Video video = em.find(Video.class, id);
		video.getCompteUploader().setNbAbonnes();
		video.getCompteUploader().setNbVideos();
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
		dbCompte.setNbAbonnes();
		dbCompte.setNbVideos();
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
		commentaire.getCompteUploader().setNbAbonnes();
		commentaire.getCompteUploader().setNbVideos();
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
	
	public boolean liked(int compteID, int videoID) {
		Compte c = getCompte(compteID);
		Video v = getVideoFromID(videoID);
		return (c!=null && v!=null && c.getVideosLike().contains(v));
	}
	
	public void addVue(Video vue) {
		vue.setNbVues(vue.getNbVues()+1);
		em.merge(vue);
	}
	
	@Override
	public Collection<Video> getHashtagVideos(String hashtag){
		Hashtag hash = em.find(Hashtag.class, hashtag);
		if (hash==null) {return null;}
		Collection<Video> videos = new LinkedList<Video>(hash.getVideos());
		for (Video video: videos) {
			video.getCompteUploader().setNbAbonnes();
			video.getCompteUploader().setNbVideos();
		}
		return videos;
	}
	
	@Override
	public boolean addAbonnement(int compteID, int abonnementID) {
		Compte compte = getCompte(compteID);
		Compte abonnement = getCompte(abonnementID);
		if (compte == null || abonnement == null || compte.getAbonnement().contains(abonnement)) return false;
		compte.addAbonnement(abonnement);
		return true;
	}
	
	@Override
	public boolean removeAbonnement(int compteID, int abonnementID) {
		Compte compte = getCompte(compteID);
		Compte abonnement = getCompte(abonnementID);
		if (compte == null || abonnement == null || !compte.getAbonnement().contains(abonnement)) return false;
		compte.removeAbonnement(abonnement);
		return true;
	}
	
	@Override
	public boolean estAbonne(int compteID, int abonnementID) {
		Compte compte = getCompte(compteID);
		Compte abonnement = getCompte(abonnementID);
		return compte != null && abonnement != null && compte.getAbonnement().contains(abonnement);
	}
	
	@Override
	public Collection<Video> getVideosFromCompte(int compteId){
		Compte compte = em.find(Compte.class, compteId);
		if (compte==null) return null;
		Collection<Video> videos = new LinkedList<Video>(compte.getVideos());
		for (Video video: videos) {
			video.getCompteUploader().setNbAbonnes();
			video.getCompteUploader().setNbVideos();
		}
		return videos;
	}
	
	public Collection<Video> getVideoFromLieu(String lieu){
		TypedQuery<Video> req = em.createQuery("SELECT v FROM Video v WHERE lieu=?1",
				Video.class);
		req.setParameter(1, lieu);
		Collection<Video> videos = req.getResultList();
		for (Video video: videos) {
			video.getCompteUploader().setNbAbonnes();
			video.getCompteUploader().setNbVideos();
		}
		return videos;
	}
	
	@Override
	public Collection<Compte> getAbonnements(int compteId) {
		Compte compte = em.find(Compte.class, compteId);
		if (compte == null) return null;
		return new LinkedList<Compte>(compte.getAbonnement());
	}
	
	@Override
	public Collection<String> getHashtags() {
		return em.createQuery("SELECT h FROM Hashtag h",Hashtag.class).getResultList().stream().map(hashtag -> hashtag.getHashtagName()).collect(Collectors.toList());
	}
}
