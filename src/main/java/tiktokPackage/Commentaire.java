package tiktokPackage;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.gson.annotations.Expose;

@Entity
public class Commentaire {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	private int id;
	
	@OneToOne(fetch=FetchType.EAGER)
	@Expose
	Compte compteUploader;
	
	@Expose
	private String text;
	
	@Expose
	private int nombreLikes;
	
	@Expose
	private Date date;

	public Commentaire() {}
	
	public Commentaire(String text, Compte uploader, int nbLikes, Date dateUpload) {
		this.text = text;
		this.compteUploader = uploader;
		this.nombreLikes = nbLikes;
		this.date = dateUpload;
	}
	
	public Compte getCompteUploader() {
		return compteUploader;
	}

	public void setCompteUploader(Compte compteUploader) {
		this.compteUploader = compteUploader;
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNombreLikes() {
		return nombreLikes;
	}

	public void setNombreLikes(int nombreLikes) {
		this.nombreLikes = nombreLikes;
	}
	
	public void addLike() {
		this.nombreLikes++;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Commentaire [ id: " + id + " ,\nuploader: " + compteUploader + " ,\ntexte: " +  text + " ,\nLikes: " + nombreLikes + " ,\ndate: " + date + "]";
	}
}
