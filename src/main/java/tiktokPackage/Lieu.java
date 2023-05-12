package tiktokPackage;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Lieu implements Serializable{

	@Id
	@Expose
	@Column(unique=true)
	private String lieuName;
	
	@OneToMany(mappedBy = "lieu")
	Set<Video> videos;
	
	public Lieu() {}

	public String getLieuName() {
		return lieuName;
	}

	public void setLieuName(String lieuName) {
		this.lieuName = lieuName;
	}

	public Set<Video> getVideos() {
		return videos;
	}
}
