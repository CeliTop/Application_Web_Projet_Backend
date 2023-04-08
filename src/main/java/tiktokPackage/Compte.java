package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@SuppressWarnings("serial")
public class Compte implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String nom;
	
	@OneToMany(mappedBy="compteUploader")
	Collection<Video> videos;
	
	public Compte() {
		
	}
	
	public Compte(String nom) {
		this.setNom(nom);
	}
	
	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Collection<Video> getVideos() {
		return videos;
	}

	public void addVideo(Video video) {
		this.videos.add(video);
		video.setCompteUploader(this);
	}
}
