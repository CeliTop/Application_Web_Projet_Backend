package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
	
	@Expose
	private String bio;
	
	@Expose
	private Date registerDate;
	
	@Expose
	private String surnom;
	
	
	@OneToMany(mappedBy="compteUploader", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Collection<Video> videos;
	
	@ManyToMany
	private Collection<Video> videosLike;
	
	@ManyToMany(mappedBy="abonnements")
	private Collection<Compte> abonnes;
	
	@ManyToMany
	private Collection<Compte> abonnements;
	
	@Expose
	@Transient
	private boolean abonne;
	
	@Expose
	@Transient
	private int nbAbonnes = 0;
	
	@Expose
	@Transient
	private int nbVideos = 0;
	
	
	
	public Collection<Video> getVideosLike() {
		return videosLike;
	}

	public Compte() {
		
	}
	
	public Compte(String nom, String password) {
		this.nom = nom;
		this.password = password;
	}
	
	public Compte(String nom, String password, String bio, Date registerDate, String surnom) {
		this.nom = nom;
		this.password = password;
		this.bio = bio;
		this.registerDate = registerDate;
		this.surnom = surnom;
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

	/*
	public void addVideo(Video video) {
		this.videos.add(video);
		video.setCompteUploader(this);
	} */
	
	public void addVideoLike(Video video) {
		videosLike.add(video);
	}
	
	public void removeVideoLike(Video video) {
		videosLike.remove(video);
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		return "Compte [ id: " + id + " ,nom: " + nom + " ]";
	}
	
	public void addAbonnement(Compte c) {
		abonnements.add(c);
	}
	
	public void removeAbonnement(Compte c) {
		abonnements.remove(c);
	}
	
	public Collection<Compte> getAbonnes() {
		return abonnes;
	}
	
	public Collection<Compte> getAbonnement() {
		return abonnements;
	}

	public boolean isAbonne() {
		return abonne;
	}

	public void setAbonne(boolean abonne) {
		this.abonne = abonne;
	}

	public void setNbAbonnes() {
		this.nbAbonnes = abonnes.size();
	}
	public void setNbVideos() {
		this.nbVideos = videos.size();
	}
	
}
