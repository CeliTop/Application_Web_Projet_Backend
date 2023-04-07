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
	public Collection<Compte> getAllComptes() {
		// TODO Auto-generated method stub
		TypedQuery<Compte> req = em.createQuery("select c from Compte c",
				Compte.class);
		return req.getResultList();
	}

	@Override
	public void addCompte(Compte c) {
		// TODO Auto-generated method stub
		em.persist(c);
	}

	@Override
	public void posterVideo(Compte c, Video video) {
		// TODO Auto-generated method stub
		
	}
	
}
