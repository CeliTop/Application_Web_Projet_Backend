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
	public void posterVideo(Compte c, Video video) {
		// TODO Faire le lien entre la vidéo et le compte
		em.persist(video);
	}
	
}
