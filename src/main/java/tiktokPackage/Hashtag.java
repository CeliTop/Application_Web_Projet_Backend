package tiktokPackage;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.google.gson.annotations.Expose;

@Entity
@SuppressWarnings("serial")
public class Hashtag implements Serializable{
	
	@Id
	@Expose
	private String hashTagName;
	
	@ManyToMany(mappedBy = "hashtags")
	private Collection<Video> videos;
	
	public Hashtag() {}

	public String getHashtagName() {
		return hashTagName;
	}

	public void setHashtagName(String hashTagName) {
		this.hashTagName = hashTagName;
	}

	public Collection<Video> getVideos() {
		return videos;
	}

	public void addVideo(Video video) {
		this.videos.add(video);
	};
}
