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

	@Column(nullable = false, unique= true)
	@Expose
	private String nom;
	
	private String password;
	
	@OneToMany(mappedBy="compteUploader", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Collection<Video> videos;
	
	public Compte() {
		
	}
	
	public Compte(String nom, String password) {
		this.nom = nom;
		this.password = password;
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

	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Compte [ id: " + id + " ,nom: " + nom + " ]";
	}
	
}
