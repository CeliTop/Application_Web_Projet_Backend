package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Hashtag implements Serializable{
	
	/*@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;*/
	
	@Id
	@Expose
	@Column(unique=true)
	private String hashTagName;
	
	@ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
	Set<Video> videos;

	public Hashtag() {}
	
	public Hashtag(String name) {
		this.hashTagName = name;
	}

	public String getHashtagName() {
		return hashTagName;
	}

	public void setHashtagName(String hashTagName) {
		this.hashTagName = hashTagName;
	}

	public Collection<Video> getVideos() {
		return videos;
	}

	/*public void addVideo(Video video) {
		this.videos.add(video);
	};*/
}
