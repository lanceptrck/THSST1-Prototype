
public class Main {

	public static void main(String[] args) {
		
		String correctSentences = "C:\\data\\correct_esrawa.txt";
		String incorrectSentences = "C:\\data\\incorrect_esrawa.txt";
		String correctTags = "C:\\data\\tags_esrawa.txt";
		String incorrectTags = "C:\\data\\incorrect_tags_esrawa.txt";
		
		// filename & ngram threshold
//		PosNgram p = new PosNgram(incorrectTags, 3);
//		p.start();
		
		//filename & ngram threshold
//		SpellingNgram n = new SpellingNgram("C:\\data\\lemmas3.txt", 3);
//		n.start();
		
//		RuleGenerator rg = new RuleGenerator(correctSentences, incorrectSentences, 3);
//		rg.start();
		
		RuleGeneration rg = new RuleGeneration(correctSentences, incorrectSentences, correctTags, incorrectTags, 3);
		rg.start();

		
	}

}
