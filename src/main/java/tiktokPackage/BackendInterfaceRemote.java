package tiktokPackage;

import java.util.Collection;

import javax.ejb.Remote;

@Remote
public interface BackendInterfaceRemote {
	public Collection<Compte> getAllComptes();
	public void addCompte(Compte c);
	public void posterVideo(Compte c, Video video);
}
