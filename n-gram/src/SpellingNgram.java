import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpellingNgram {
	
	private String file;
	private List<String> lemmas;
	private List<String> lemmasTags;
	private ArrayList<String> spellingResults;
	private Set<String> setOfSpelling;
	private HashMap<String, Integer> spellFrequency;
	private Map<String, Integer> treeMap;
	private WriteFile wf;
	private ReadFile rf;
	private double threshold;
	private int gram;
	
	public SpellingNgram(String file, int gram){
		
		lemmas = new ArrayList<String>();
		lemmasTags = new ArrayList<String>();
		spellingResults = new ArrayList<String>();
		setOfSpelling = new HashSet<String>();
		spellFrequency = new HashMap<String, Integer>();
		rf = new ReadFile(file);
		this.file = file;
		this.gram = gram;
	}
	
	public void start(){
		init();
		this.computeNgrams();
		write();
	}
	
	public void init(){
		
		try {
			lemmas = rf.OpenFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void computeNgrams()
	{
		for(int i = 0; i<lemmas.size(); i++){
			WordSplit ws = new WordSplit(lemmas.get(i));
					
				for(int k = 0; k<ws.getWords().length; k++){
					NgramWordIterator ngwi = new NgramWordIterator(3, ws.getWords()[k]);
					
					while(ngwi.hasNext()){
						String s = ngwi.next();
						spellingResults.add(s);
						setOfSpelling.add(s);
					}
						
				}
					
		}
		
		for(String s : setOfSpelling){
			spellFrequency.put(s, 0);
		}
			
		
		for(String s: spellingResults){
			spellFrequency.replace(s, spellFrequency.get(s)+1);
		}
			
		
		Map<String, Integer> treeMap = sortByValue(spellFrequency);
		
		spellingResults.clear();
		for(Map.Entry<String, Integer> entry : treeMap.entrySet()){
			if(entry.getValue() >= 1){
				//System.out.println(entry.getKey() + "/" + entry.getValue());
				spellingResults.add(entry.getKey() + "/" + entry.getValue());
			}
		}
	}
	
	public void write()
	{
		System.out.println();
		wf = new WriteFile(file.substring(0, file.length()-4)+"_spelling_results.txt");
		try {
			wf.write(spellingResults);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
