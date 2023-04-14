package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Compte implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	private int id;

	@Column(nullable = false)
	@Expose
	private String nom;
	
	@OneToMany(mappedBy="compteUploader", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Collection<Video> videos;
	
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
		System.out.println(this.videos);
		this.videos.add(video);
		System.out.println(this.videos);
		video.setCompteUploader(this);
	}
}
