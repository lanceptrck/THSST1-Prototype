import java.util.*;

public class Pairing {
	
	private List<String> tags;
	private List<String> words;
	private Set<String> wordsAndTags;
	
	public Pairing(){
		
		tags = new ArrayList<String>();
		words = new ArrayList<String>();
		wordsAndTags = new HashSet<String>();
		
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public Set<String> getWordsAndTags() {
		return wordsAndTags;
	}

	public void setWordsAndTags(Set<String> wordsAndTags) {
		this.wordsAndTags = wordsAndTags;
	}
	
	

}