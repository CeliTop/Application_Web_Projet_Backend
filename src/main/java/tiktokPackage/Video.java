package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Video  implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	private int id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Expose
	Compte compteUploader;
	
	@OneToMany(fetch=FetchType.EAGER)
	@Expose
	Collection<Commentaire> commentaires;
	
	@ManyToMany(mappedBy = "hashtags")
	private Collection<Hashtag> hashtags;

	@Expose
	private String description;
	
	@Expose
	private String lieu;
	
	@Expose
	private int nbVues;
	
	@Expose
	private int nbLikes;
	
	public Video() {}
	
	public int getId() {
		return id;
	}
	
	public Compte getCompteUploader() {
		return compteUploader;
	}
	
	public void like() {
		nbLikes++;
	}
	
	public void unlike() {
		nbLikes--;
	}
	
	public void setCompteUploader(Compte compteUploader) {
		this.compteUploader = compteUploader;
	}
	
	public Collection<Commentaire> getCommentaires() {
		return commentaires;
	}
	
	public void addCommentaire(Commentaire commentaire) {
		this.commentaires.add(commentaire);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public int getNbVues() {
		return nbVues;
	}

	public void setNbVues(int nbVues) {
		this.nbVues = nbVues;
	}
	
	@Override
	public String toString() {
		return "Video [ id: " + id + " ,uploader: " + compteUploader.toString() + " ]";
	}
	
	public Collection<Hashtag> gethashtags() {
		return hashtags;
	}
	
	public void addHashtag(Hashtag hashtag) {
		hashtags.add(hashtag);
	}
}
