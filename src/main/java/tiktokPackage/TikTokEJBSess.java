package tiktokPackage;

import java.util.Collection;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Singleton
public class TikTokEJBSess implements BackendInterfaceLocal, BackendInterfaceRemote{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void addCompte(Compte c) {
		System.out.println("========== Compte =========");
		System.out.println(c.getId());
		em.persist(c);
	}
	
	@Override
	public Compte getCompte(int id) {
		Compte compte = em.find(Compte.class, id);
		return compte;
	}

	@Override
	public Collection<Compte> getAllComptes() {
		TypedQuery<Compte> req = em.createQuery("select c from Compte c",
				Compte.class);
		return req.getResultList();
	}

	@Override
	public Video posterVideo(Compte c, Video video) {
		// TODO Faire le lien entre la vidéo et le compte
		em.persist(video);
		System.out.println("========== Video =========");
		System.out.println(video.toString());
		Compte cdatabase = em.find(Compte.class, c.getId());
		System.out.println(cdatabase);
		System.out.println(video);
		cdatabase.addVideo(video);
		return video;
	}
	
	public Video getRandomVIdeo() {
		TypedQuery<Video> req = em.createQuery("select v from Video v ORDER BY RAND()",Video.class).setMaxResults(1);
		return req.getResultList().get(0);
	}
	
	public Video getVideoFromID(int id) {
		Video video = em.find(Video.class, id);
		return video;
	}
	
}
