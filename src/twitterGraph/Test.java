package twitterGraph;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) throws Exception {
		
	SearchAndSave search = new SearchAndSave();
	
	search.init();
	
	List<String> list =  new ArrayList<String>();
	
	list.add("Fabian");
	list.add("Microsoft");
	list.add("Miroku");
	list.add("Twitter");
	list.add("Facebook");
	
	search.popularTopicKeywords(list, 1);

	}

}