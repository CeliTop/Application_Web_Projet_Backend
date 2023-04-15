package tiktokPackage;

import java.util.Collection;

import javax.ejb.Local;

@Local
public interface BackendInterfaceLocal {
	public void addCompte(Compte c);
	public Compte getCompte(int id);
	public Collection<Compte> getAllComptes();
	public Video posterVideo(Compte c, Video video);
	public Video getRandomVIdeo();
	public Video getVideoFromID(int id);
}
