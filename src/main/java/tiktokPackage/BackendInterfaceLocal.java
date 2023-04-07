package tiktokPackage;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface BackendInterfaceLocal {
	public Collection<Compte> getAllComptes();
	public void addCompte(Compte c);
	public void posterVideo(Compte c, Video video);
}
