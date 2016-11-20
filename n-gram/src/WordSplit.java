
public class WordSplit {
	
	String[] words;
	
	public WordSplit(String str){
		words = str.split(" ");
	}

	public String[] getWords() {
		return words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}
	
	

}
