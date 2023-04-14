package tiktokPackage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Video  implements Serializable{
	
	//BONJOUR
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Expose
	private int id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@Expose
	Compte compteUploader;

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
	public String toString() {
		return "Video id: " + id + " uploader: " + compteUploader;
	}

}
