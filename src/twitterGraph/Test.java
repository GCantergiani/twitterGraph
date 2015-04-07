package twitterGraph;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) throws Exception {
		
	SearchPopularKeywords search = new SearchPopularKeywords();
	
	search.init();
	
	List<String> list =  new ArrayList<String>();
	
	list.add("Fabian");
	list.add("Microsoft");
	list.add("Miroku");
	list.add("Twitter");
	list.add("Facebook");
	
	search.searchPopular(list, 1);

	}

}