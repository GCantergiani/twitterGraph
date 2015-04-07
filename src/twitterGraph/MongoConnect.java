package twitterGraph;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class MongoConnect {
	private final Datastore db;
	public static final String DB_NAME = "twitterGraph";
	private MongoClient mongoClient;

	public MongoConnect() {
		try {
			mongoClient = new MongoClient("localhost");
			db = new Morphia().map(Nodo.class).createDatastore(mongoClient,
					DB_NAME);
			db.ensureIndexes();
		} catch (Exception e) {
			throw new RuntimeException("Error initializing mongo db", e);
		}
	}

	public Datastore getDb() {
		return db;
	}
	
	
	public void close(){
		mongoClient.close();
	}
}