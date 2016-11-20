import java.io.IOException;
import java.util.*;

public class RuleGenerator {
	
	private String correctCorpus;
	private String incorrectCorpus;
	private List<String> correctTags;
	private List<String> incorrectTags;
	private List<String> unfilteredTags;
	private List<String> incorrectUnfilteredTags;
	private ArrayList<String> results;
	private ArrayList<String> incorrectResults;
	private Set<String> setOfTags;
	private Set<String> incorrectSetOfTags;
	private ArrayList<Pairing> correctPairings;
	private ArrayList<Pairing> incorrectPairings;
	private HashMap<String, Integer> tagFrequency;
	private Map<String, Integer> treeMap;
	private WriteFile wf;
	private ReadFile rf;
	private double threshold;
	private int gram;
	private String typeOfError[] = {"DELETION", "INSERTION", "SUBSTIUTION"};
	
	public RuleGenerator(String correct, String incorrect, int gram)
	{
		correctTags = new ArrayList<String>();
		incorrectTags = new ArrayList<String>();
		unfilteredTags = new ArrayList<String>();
		incorrectUnfilteredTags = new ArrayList<String>();
		results = new ArrayList<String>();
		incorrectResults = new ArrayList<String>();
		setOfTags = new HashSet<String>();
		incorrectSetOfTags = new HashSet<String>();
		this.correctCorpus = correct;
		this.incorrectCorpus = incorrect;
		this.gram = gram;
	}
	
	public void start()
	{
		init();
		generateNgrams();
		printNgrams();
	}
	
	public void init(){
		
		rf = new ReadFile(correctCorpus);
		
		try {
			correctTags = rf.OpenFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rf = new ReadFile(incorrectCorpus);
		
		try {
			incorrectTags = rf.OpenFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void generateNgrams()
	{
		for(int i = 0; i<correctTags.size(); i++){
			NgramIterator c_n = new NgramIterator(gram, correctTags.get(i));
			NgramIterator i_n = new NgramIterator(gram, incorrectTags.get(i));
			
				while(c_n.hasNext()){
					String extracted = c_n.next();
					setOfTags.add(extracted);
					unfilteredTags.add(extracted);
				}
				
				while(i_n.hasNext()){
					String extracted = i_n.next();
					incorrectSetOfTags.add(extracted);
					incorrectUnfilteredTags.add(extracted);
				}
			}
			
		ArrayList<String> list = new ArrayList<String>();
		for(String s : incorrectSetOfTags){
			if(!setOfTags.contains(s)){
				list.add(s);
			}
		}
		
		write(list);
			
	}
	
	public void generalizeErrors(){
		for(int i = 0; i<(correctTags.size()+incorrectTags.size())/2; i++){
			
			WordSplit c_ws, i_ws;
			
			c_ws = new WordSplit(correctTags.get(i));
			i_ws = new WordSplit(incorrectTags.get(i));
			
			System.out.print((i+1)+" ");
			if(c_ws.getWords().length < i_ws.getWords().length)
				System.out.println("INSERTION(WRONG LIGATURE, EXTRA WORD, SPELLING[EXTRA SPACE])");
			else if(c_ws.getWords().length > i_ws.getWords().length)
				System.out.println("DELETION(MISSING WORD, SPELLING(NO SPACE)");
			else
				System.out.println("SUBSTITUTION(ENCLITICS, NG or NANG, SPELLING[HYPHENATION])");
			
			
			
		}
	}
	
	public void printNgrams(){
		
//		for(String s : unfilteredTags){
//			System.out.println(s);
//		}
		
		System.out.println(correctTags.size() +" "+incorrectTags.size());
		
	}
	
	public void write(ArrayList<String> list){
		
		wf = new WriteFile("C:\\data\\faulty words.txt");
		
		try {
			wf.write(list);
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
	

