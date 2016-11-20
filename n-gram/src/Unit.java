import java.util.*;

public class Unit {

	private String sentence;
	private ArrayList<String> posNgrams;
	private ArrayList<String> wordNgrams;
	private Set<String> wordsAndTags;
	private int gram;
	private String errorType;
	private String generalizeHybridNgram;

	public Unit(String sentence, ArrayList<String> posNgrams, ArrayList<String> wordNgrams, Set<String> wordsAndTags,
			int gram) {
		super();
		this.sentence = sentence;
		this.posNgrams = posNgrams;
		this.wordNgrams = wordNgrams;
		this.wordsAndTags = wordsAndTags;
		this.gram = gram;
	}
	
	

	public Unit(String sentence, ArrayList<String> posNgrams, ArrayList<String> wordNgrams, int gram) {
		super();
		this.sentence = sentence;
		this.posNgrams = posNgrams;
		this.wordNgrams = wordNgrams;
		this.gram = gram;
	}



	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public ArrayList<String> getPosNgrams() {
		return posNgrams;
	}

	public void setPosNgrams(ArrayList<String> posNgrams) {
		this.posNgrams = posNgrams;
	}

	public ArrayList<String> getWordNgrams() {
		return wordNgrams;
	}

	public void setWordNgrams(ArrayList<String> wordNgrams) {
		this.wordNgrams = wordNgrams;
	}

	public int getGram() {
		return gram;
	}

	public void setGram(int gram) {
		this.gram = gram;
	}

	public Set<String> getWordsAndTags() {
		return wordsAndTags;
	}

	public void setWordsAndTags(Set<String> wordsAndTags) {
		this.wordsAndTags = wordsAndTags;
	}



	public String getErrorType() {
		return errorType;
	}



	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
	
	
	
	
	
	
	
}
