package tiktokPackage;

import java.util.Collection;

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
	public boolean addCompte(Compte c) {
		try {	
			em.persist(c);
			return true;
		} catch (PersistenceException e) {
			return false;
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
	public Video posterVideo(Compte c, Video video) {
		// TODO Faire le lien entre la vidéo et le compte
		em.persist(video);
		Compte cdatabase = em.find(Compte.class, c.getId());
		cdatabase.addVideo(video);
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
		Compte dbCompte = req.getResultList().get(0);
		if (dbCompte == null || !(dbCompte.getPassword().equals(c.getPassword()))) return null;
		return dbCompte;
	}
	
}
