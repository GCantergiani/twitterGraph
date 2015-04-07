package twitterGraph;

import java.util.List;
import java.util.Vector;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity(value = "nodos")
public class Nodo {
	
	@Id
	private ObjectId id;
	private Long twitterId;
	private String screenName= "";
	private List<Long> following = new Vector<Long>();
	private List<Long> followers = new Vector<Long>();
	private boolean verified;
	private String name = "";
	private String lang = "";
	private int followersCount;
	private int friendCount;
	private String Location = "";
	private int lvl;

	public Nodo() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(Long string) {
		this.twitterId = string;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public List<Long> getFollowing() {
		return following;
	}
	
	public void addFollowing(Long id) {
		following.add(id);
	}

	public List<Long> getFollowers() {
		return followers;
	}
	
	public void addFollowers(Long id) {
		followers.add(id);
	}


	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	
	@Override
	public String toString() {
		return "Nodo: id:" + twitterId;
		// return "Customer [firstName=" + firstName + ", id=" + id +
		// ", lastName=" + lastName + "]";
	}
}