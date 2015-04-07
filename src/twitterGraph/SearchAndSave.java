package twitterGraph;

import java.util.List;
import java.util.Map;

import twitter4j.PagableResponseList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class SearchAndSave {

	private final static int FOLLOWER_PODA = 10;
	private final static int FRIEND_PODA = 10;

	private Twitter twitter;
	private MongoDao md;

	public void init() {

		twitter = new TwitterFactory().getInstance();
		md = new MongoDao();
	}

	public void byId(long id) {
		if (twitter == null && md == null) {
			twitter = new TwitterFactory().getInstance();
			md = new MongoDao();
		}

		for (Long follower : md.getFollowersById(id)) {
			try {
				User user = twitter.showUser(follower);
				searchFL(user);
			} catch (TwitterException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Long following : md.getFollowingById(id)) {
			try {
				User user = twitter.showUser(following);
				searchFL(user);
			} catch (TwitterException | InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public void popularTopicKeywords(List<String> words, int pages) {

		if (twitter == null && md == null) {
			twitter = new TwitterFactory().getInstance();
			md = new MongoDao();
		}

		String searchQuery = "";

		for (int i = 0; i < words.size(); i++) {
			if (i == words.size() - 1) {
				searchQuery = searchQuery.concat("(").concat(words.get(i))
						.concat(")");
			} else {
				searchQuery = searchQuery.concat("(").concat(words.get(i))
						.concat(")").concat(" OR ");
			}
		}

		Query query = new Query(searchQuery);
		query.resultType(Query.POPULAR);
		query.setCount(pages);
		QueryResult result = null;
		do {
			try {
				Map<String, RateLimitStatus> rateLimitStatus = twitter
						.getRateLimitStatus();
				RateLimitStatus status = rateLimitStatus.get("/search/tweets");
				if (status.getRemaining() == 0) {
					System.out
							.println("----------------------------------------------------------------");
					System.out
							.println("/search/tweets | status.getRemaining() :"
									+ status.getRemaining());
					System.out.println("sleep: "
							+ status.getSecondsUntilReset() + "(s)");
					Thread.sleep(status.getSecondsUntilReset() * 1000);
				}

				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					System.out.println("- tweet.getText(): " + tweet.getText());
					User userSrc = tweet.getUser();
					status = rateLimitStatus.get("/followers/list");
					if (status.getRemaining() == 0) {
						System.out
								.println("----------------------------------------------------------------");
						System.out
								.println("/friends/list | status.getRemaining() :"
										+ status.getRemaining());
						System.out.println("sleep: "
								+ status.getSecondsUntilReset() + "(s)");
						Thread.sleep(status.getSecondsUntilReset() * 1000);
					}

					searchFL(userSrc);

					status = rateLimitStatus.get("/friends/list");

					if (status.getRemaining() == 0) {
						System.out
								.println("----------------------------------------------------------------");
						System.out
								.println("/friends/list | status.getRemaining() :"
										+ status.getRemaining());
						System.out.println("sleep: "
								+ status.getSecondsUntilReset() + "(s)");
						Thread.sleep(status.getSecondsUntilReset() * 1000);

					}
					searchFR(userSrc);

				}

			} catch (TwitterException | InterruptedException e) {
				e.printStackTrace();
			}

		} while ((query = result.nextQuery()) != null);

		md.close();

	}

	private void searchFL(User user) throws TwitterException,
			InterruptedException {
		long cursorFollowers = -1;
		PagableResponseList<User> followers;
		int ff_received = 0;
		do {

			Map<String, RateLimitStatus> rateLimitStatus = twitter
					.getRateLimitStatus();
			RateLimitStatus status = rateLimitStatus.get("/followers/list");
			if (status.getRemaining() == 1) {
				System.out
						.println("----------------------------------------------------------------");
				System.out.println("/friends/list | status.getRemaining() :"
						+ status.getRemaining());
				System.out.println("sleep: " + status.getSecondsUntilReset()
						+ "(s)");
				Thread.sleep(status.getSecondsUntilReset() * 1000);
			}

			followers = twitter.getFollowersList(user.getId(), cursorFollowers);
			ff_received += followers.size();
			for (User follower : followers) {
				md.insertFL(user, follower, 0);

			}
			if (ff_received > FOLLOWER_PODA) {
				break;
			}

		} while ((cursorFollowers = followers.getNextCursor()) != 0);

	}

	private void searchFR(User user) throws TwitterException,
			InterruptedException {
		long cursorFollowers = -1;
		PagableResponseList<User> friends;
		int fr_received = 0;
		do {

			Map<String, RateLimitStatus> rateLimitStatus = twitter
					.getRateLimitStatus();
			RateLimitStatus status = rateLimitStatus.get("/friends/list");
			if (status.getRemaining() == 1) {
				Thread.sleep(status.getSecondsUntilReset() * 1000);
			}

			friends = twitter.getFriendsList(user.getId(), cursorFollowers);
			fr_received += friends.size();
			for (User friend : friends) {
				System.out.println("user: " + user + " | follower:" + friend);
				md.insertFL(user, friend, 0);
			}
			if (fr_received > FRIEND_PODA) {
				break;
			}

		} while ((cursorFollowers = friends.getNextCursor()) != 0);

	}

}
