import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RuleGeneration {
	
	private Pairing correctPairings;
	private Pairing incorrectPairings;
	private List<String> correctSentences;
	private List<String> incorrectSentences;
	private List<String> correctTags;
	private List<String> incorrectTags;
	private ArrayList<String> results;
	private Set<String> ngramWordSet;
	private Set<String> incorrectNgramWordSet;
	private List<Unit> unitList;
	private List<Unit> incorrectUnitList;
	private ArrayList<String> faultyHybridNgrams;
	
	private String correctCorpus;
	private String incorrectCorpus;
	private String correctTagCorpus;
	private String incorrectTagCorpus;
	
	private ReadFile rf;
	private WriteFile wf;
	
	private int gram;
	
	public RuleGeneration(String cc, String ic, String ctc, String itc, int gram){
		
		this.correctCorpus = cc;
		this.incorrectCorpus = ic;
		this.correctTagCorpus = ctc;
		this.incorrectTagCorpus = itc;
		this.gram = gram;
		
		correctPairings = new Pairing();
		incorrectPairings = new Pairing();
		
		ngramWordSet = new HashSet<String>();
		incorrectNgramWordSet = new HashSet<String>();
		
		unitList = new ArrayList<Unit>();
		incorrectUnitList = new ArrayList<Unit>();
		
		faultyHybridNgrams = new ArrayList<String>();
	}
	
	public void start(){
		init();
		preProcess();
		processPairings();
		comparePairings();
	}
	
	public void init()
	{
		// for correct
		rf = new ReadFile(correctCorpus);
		try {
			correctSentences = rf.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		rf = new ReadFile(correctTagCorpus);
		try {
			correctTags = rf.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//for incorrect
		rf = new ReadFile(incorrectCorpus);
		try {
			incorrectSentences = rf.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rf = new ReadFile(incorrectTagCorpus);
		try {
			incorrectTags = rf.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	public void preProcess()
	{
		
		for(int i = 0; i<correctSentences.size(); i++){
			correctSentences.set(i, "<empty> <start> "+correctSentences.get(i)+" <end> <empty>");
			correctTags.set(i, "<empty> <start> "+correctTags.get(i)+" <end> <empty>");
			incorrectSentences.set(i, "<empty> <start> "+incorrectSentences.get(i)+" <end> <empty>");
			incorrectTags.set(i, "<empty> <start> "+incorrectTags.get(i)+" <end> <empty>");
		
		}
	}
	
	public void processPairings()
	{
		List<String> s_List = new ArrayList<String>();
		List<String> t_List = new ArrayList<String>();
		Set<String> st_List = new HashSet<String>();
		
		for(int i = 0; i<correctTags.size(); i++){
			NgramIterator ngi_sentences = new NgramIterator(gram, correctSentences.get(i));
			NgramIterator ngi_tags = new NgramIterator(gram, correctTags.get(i));
			ArrayList<String> s_ListTemp = new ArrayList<String>();
			ArrayList<String> t_ListTemp = new ArrayList<String>();
			Set<String> localSet = new HashSet<String>();
			
				while(ngi_sentences.hasNext()){
					String s_extracted = ngi_sentences.next();
					String t_extracted = ngi_tags.next();
					//System.out.println(s_extracted+"/"+t_extracted);
					s_List.add(s_extracted);
					s_ListTemp.add(s_extracted);
					t_List.add(t_extracted);
					t_ListTemp.add(t_extracted);
					st_List.add(s_extracted+" / "+t_extracted);
					localSet.add(s_extracted+" / "+t_extracted);
					ngramWordSet.add(s_extracted+" / "+t_extracted);
				}
				
				unitList.add(new Unit(correctSentences.get(i), t_ListTemp, s_ListTemp, localSet, gram));
		}
		
		correctPairings.setWords(s_List);
		correctPairings.setTags(t_List);
		correctPairings.setWordsAndTags(st_List);
		s_List.clear();
		t_List.clear();
		st_List.clear();
		
		for(int i = 0; i<incorrectTags.size(); i++){
			NgramIterator ngi_sentences = new NgramIterator(gram, incorrectSentences.get(i));
			NgramIterator ngi_tags = new NgramIterator(gram, incorrectTags.get(i));
			ArrayList<String> s_ListTemp = new ArrayList<String>();
			ArrayList<String> t_ListTemp = new ArrayList<String>();
			Set<String> localSet = new HashSet<String>();
			
				while(ngi_sentences.hasNext()){
					String s_extracted = ngi_sentences.next();
					String t_extracted = ngi_tags.next();
					//System.out.println(s_extracted+"/"+t_extracted);
					s_List.add(s_extracted);
					s_ListTemp.add(s_extracted);
					t_List.add(t_extracted);
					t_ListTemp.add(t_extracted);
					st_List.add(s_extracted+" / "+t_extracted);
					localSet.add(s_extracted+" / "+t_extracted);
					incorrectNgramWordSet.add(s_extracted+" / "+t_extracted);
				}
				
				incorrectUnitList.add(new Unit(incorrectSentences.get(i), t_ListTemp, s_ListTemp, localSet, gram));
		}
		
		incorrectPairings.setWords(s_List);
		incorrectPairings.setTags(t_List);
		incorrectPairings.setWordsAndTags(st_List);
		
				
	}
	
	public void comparePairings(){
		
	results = new ArrayList<String>();
//		for(String s : incorrectNgramWordSet){
//				if(!ngramWordSet.contains(s))
//					results.add(s);
//		}
		
		for(int i = 0; i<unitList.size(); i++){
			Unit u = unitList.get(i);
			Unit iu	= incorrectUnitList.get(i);	
			System.out.println(u.getSentence()+" / "+iu.getSentence());
			results.add(u.getSentence()+" / "+iu.getSentence());
//			for(int j = 0; j<Math.max(u.getPosNgrams().size(), iu.getPosNgrams().size()) ; j++){
//				
//				String s = "", t = "";
//				if(j<u.getPosNgrams().size()){
//					s = u.getPosNgrams().get(j);
//					t= u.getWordNgrams().get(j);
//				} 
//				
//				String s2 = "", t2 = "";
//				if(j<iu.getPosNgrams().size()){
//					s2 = iu.getPosNgrams().get(j);
//					t2= iu.getWordNgrams().get(j);
//				} 
//				
//				System.out.println("["+s+"|"+t+"]"+"/"+"["+s2+"|"+t2+"]");
//					
//			}
			
			int differenceCounter = 0;
			Set<String> local = new HashSet<String>();
			for(String s : iu.getWordNgrams()){ // CAN BE INTERCHANGED TO POS, OR POS+WORDS
				
				if(!u.getWordNgrams().contains(s)){ // CAN BE INTERCHANGED TO POS, OR POS+WORDS
					results.add(s);
					//System.out.println(s);
					differenceCounter++;
					local.add(s);
				}
			
			}
			
			String token = generalize(local);
			//See if token is just an enclitic
			String token2 = generalize(token);
			
			
			Set<String> local2 = new HashSet<String>();
			for(String s : iu.getPosNgrams()){ // CAN BE INTERCHANGED TO POS, OR POS+WORDS
				
				if(!u.getPosNgrams().contains(s)){ // CAN BE INTERCHANGED TO POS, OR POS+WORDS
					local2.add(s);
				}
			
			}
			
			//override token2
			if(local2.size()>0)
				token2 = generalize(local2);
			else {
				for(String s : iu.getPosNgrams()){ // CAN BE INTERCHANGED TO POS, OR POS+WORDS
					if(s.contains(token2)){
						local2.add(s);
					}
				
				}
			}
			
			generateHybrid(token, token2, local2);
			
			
			if(differenceCounter == gram-1){
				iu.setErrorType("DELETION/MERGING");
				System.out.println("DELETION/MERGING");
				results.add("DELETION/MERGING");
			} else if(differenceCounter == gram+1){
				iu.setErrorType("INSERTION/UNMERGING");
				System.out.println("INSERTION/MERGING");
				results.add("INSERTION/UNMERGING");
			}
			else if(differenceCounter%gram == 1 || differenceCounter%gram == 2){
				iu.setErrorType("INSERTION/EXTRA WORD/UNMERGING");
				System.out.println("INSERTION/EXTRA WORD/UNMERGING");
				results.add("INSERTION/EXTRA WORD/UNMERGING");
			} else if(differenceCounter%gram == 0){
				iu.setErrorType("SPELLING/SUBSTITUTION");
				System.out.println("SPELLING/SUBSTITUTION");
				results.add("SPELLING/SUBSTITUTION");
			} System.out.println("Difference counter: "+differenceCounter);
			
			System.out.println();
			
		}
		
		frequencyHybridNgrams();

		
	}
	
	public String generalize(String s)
	{
		switch(s.toLowerCase()){
			case "ng": return "CCB";
			case "nang": return "CCB";
			case "rin": return "RBI";
			case "din": return "RBI";
		}
		
		return "";
	}
	
	public void generateHybrid(String token, String token2, Set<String> tags)
	{
		System.out.println(token+"/"+token2);

		for(String s : tags){
			
			String temp[] = s.split(" ");
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i<temp.length; i++){
				if(temp[i].equals(token2)){
					temp[i] = token;
				}
					
					if(i == temp.length-1)
						sb.append(temp[i]);
					else sb.append(temp[i]+" ");
			}
			
			System.out.println(sb.toString());
			faultyHybridNgrams.add(sb.toString());
		}
		
	}
	
	public void frequencyHybridNgrams(){
		
		Map<String, Integer> ngrams = new HashMap<String, Integer>();
		Set<String> frequently = new HashSet<String>();
		
		for(String s : faultyHybridNgrams){
			frequently.add(s);
		}
		
		for(String s : frequently){
			ngrams.put(s, 0);
		}
		
		for(String s : faultyHybridNgrams){
			ngrams.replace(s, ngrams.get(s)+1);
		}
		
		ngrams = sortByValue(ngrams);
		
		ArrayList<String> mapping = new ArrayList<String>();
		for(Entry<String, Integer> entry : ngrams.entrySet()){
			mapping.add(entry.getKey()+"/"+entry.getValue());
//			System.out.println(entry.getKey()+"/"+entry.getValue());
		}	
		
		write(mapping, "C:\\data\\ngramFrequency.txt");
		
		
	}
	
	public String generalize(Set<String> set){
		
		ArrayList<String> fucking = new ArrayList<String>();
		Set<String> frequently = new HashSet<String>();
		Map<String, Integer> words = new HashMap<String, Integer>();
		
		for(String s : set){
			String gg[] = s.split(" ");
			for(int i = 0; i<gg.length; i++){
				if(!gg[i].equals("/")){
					fucking.add(gg[i]);
					frequently.add(gg[i]);
				}
					
			}
		}
		
		for(String s : frequently){
			words.put(s, 0);
		}
		
		for(String s : fucking){
			words.replace(s, words.get(s)+1);
		}
		
		
		Entry<String,Integer> maxEntry = null;
		for(Entry<String,Integer> entry : words.entrySet()) {
		    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) 
		        maxEntry = entry;	    
		}
		
		System.out.println(maxEntry);
		

			int occurence = maxEntry.getValue();
			ArrayList<Entry> temp = new ArrayList<Entry>();
		
		int counter = 0;
		for(Entry<String,Integer> entry : words.entrySet()) {
		    if(entry.getValue() == occurence)
		    {
		    	temp.add(entry);
		    }
		}	
		
		if(temp.size() > 1){
			
		}
		
		
		return maxEntry.getKey();
		
	}
	
	public String getPriorityWord()
	{
		
		
		return "";
	}
	
	
	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }
	
	public void write(ArrayList<String> toWrite, String path){
		
		wf = new WriteFile(path);
		try {
			wf.write(toWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
