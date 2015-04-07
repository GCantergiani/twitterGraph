package twitterGraph;

import java.util.List;

import org.mongodb.morphia.Datastore;

import twitter4j.User;

public class MongoDao {

	private Datastore db;
	private MongoConnect mc;

	public MongoDao() {
		mc = new MongoConnect();
		db = mc.getDb();
	}

	public void insertFL(User userSrc, User userDest, int lvl) {

		Nodo nodoSrc = db.find(Nodo.class).field("twitterId")
				.equal(userSrc.getId()).get();
		if (nodoSrc == null) {

			nodoSrc = new Nodo();
			nodoSrc.setFollowersCount(userSrc.getFollowersCount());
			nodoSrc.setFriendCount(userSrc.getFriendsCount());
			nodoSrc.setLang(userSrc.getLang());
			nodoSrc.setLocation(userSrc.getLocation());
			nodoSrc.setLvl(lvl);
			nodoSrc.setName(userSrc.getName());
			nodoSrc.setScreenName(userSrc.getScreenName());
			nodoSrc.setTwitterId(userSrc.getId());
			nodoSrc.setVerified(userSrc.isVerified());

			nodoSrc.addFollowers(userDest.getId());
			db.save(nodoSrc);

		} else {
			nodoSrc.addFollowers(userDest.getId());
			db.save(nodoSrc);
		}

	}

	public void insertFR(User userSrc, User userDest, int lvl) {

		Nodo nodoSrc = db.find(Nodo.class).field("twitterId")
				.equal(userSrc.getId()).get();
		if (nodoSrc == null) {

			nodoSrc = new Nodo();
			nodoSrc.setFollowersCount(userSrc.getFollowersCount());
			nodoSrc.setFriendCount(userSrc.getFriendsCount());
			nodoSrc.setLang(userSrc.getLang());
			nodoSrc.setLocation(userSrc.getLocation());
			nodoSrc.setLvl(lvl);
			nodoSrc.setName(userSrc.getName());
			nodoSrc.setScreenName(userSrc.getScreenName());
			nodoSrc.setTwitterId(userSrc.getId());
			nodoSrc.setVerified(userSrc.isVerified());

			nodoSrc.addFollowing(userDest.getId());
			db.save(nodoSrc);

		} else {
			nodoSrc.addFollowing(userDest.getId());
			db.save(nodoSrc);
		}

	}

	public List<Long> getFollowersById(Long id) {

		return db.find(Nodo.class).field("twitterId").equal(id).get()
				.getFollowers();

	}

	public List<Long> getFollowingById(Long id) {

		return db.find(Nodo.class).field("twitterId").equal(id).get()
				.getFollowing();

	}

	public void close() {
		mc.close();
	}

}
