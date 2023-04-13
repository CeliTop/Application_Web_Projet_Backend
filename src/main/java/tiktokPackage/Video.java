package tiktokPackage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@SuppressWarnings("serial")
public class Video  implements Serializable{
	
	//BONJOUR
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	// The filepath must be unique to not overwrite
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String fileName;
	
	@Column(nullable = false)
	private String filePath;
	
	@ManyToOne(fetch=FetchType.LAZY)
	Compte compteUploader;

	public Video() {}
	
	public int getId() {
		return id;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Compte getCompteUploader() {
		return compteUploader;
	}
	public void setCompteUploader(Compte compteUploader) {
		this.compteUploader = compteUploader;
	}
	
}
