package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

	public Video() {}
	
	public int getId() {
		return id;
	}
	public Compte getCompteUploader() {
		return compteUploader;
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
	@Override
	public String toString() {
		return "Video [ id: " + id + " ,uploader: " + compteUploader.toString() + " ]";
	}
}
