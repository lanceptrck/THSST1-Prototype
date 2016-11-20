import java.io.IOException;
import java.util.*;

public class PosNgram {
	
	private String correctCorpus;
	private List<String> tags;
	private List<String> unfilteredTags;
	private ArrayList<String> results;
	private Set<String> setOfTags;
	private HashMap<String, Integer> tagFrequency;
	private Map<String, Integer> treeMap;
	private WriteFile wf;
	private ReadFile rf;
	private double threshold;
	private int gram;
	
	public PosNgram(String correct, int gram){
		
		tags = new ArrayList<String>();
		unfilteredTags = new ArrayList<String>();
		results = new ArrayList<String>();
		setOfTags = new HashSet<String>();
		tagFrequency = new HashMap<String, Integer>();
		rf = new ReadFile(correct);
		this.correctCorpus = correct;
		this.gram = gram;
	}
	
	public void start(){
		init();
		this.computeNgrams();
		write();
	}
	
	public void init(){
		
		try {
			tags = rf.OpenFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i<tags.size(); i++){
			tags.set(i, "<start> "+tags.get(i)+" <end>");
		
		}
	}
		
	public void computeNgrams()
	{
		System.out.println("Computing number of unique n-grams....");
		for(int i = 0; i<tags.size(); i++){
			NgramIterator n = new NgramIterator(gram, tags.get(i));
			

				while(n.hasNext()){
					String extracted = n.next();
					setOfTags.add(extracted);
					unfilteredTags.add(extracted);
				}
			}
		
		System.out.println("Number of unique "+gram+"-grams "+setOfTags.size());
		System.out.println("Number of n-grams "+unfilteredTags.size());
		threshold = setOfTags.size()*0.001;
		threshold = (Math.floor(threshold)/100.0)*100.0;
		
		
		for(String s : setOfTags){
			tagFrequency.put(s, 0);
		}
		
		for(String s: unfilteredTags){
				tagFrequency.replace(s, tagFrequency.get(s)+1);
		}
		
		treeMap = sortByValue(tagFrequency);

		for(Map.Entry<String, Integer> entry1 : treeMap.entrySet()){
		if(entry1.getValue() >= 1){
			//System.out.println(entry1.getKey() + "/" + entry1.getValue());
			results.add(entry1.getKey() + "/" + entry1.getValue());
		}

	}
		
		
		System.out.println("Threshold of: "+threshold+" occurence among "+setOfTags.size()+" unique tags");
		results.add("Threshold of: "+threshold+" occurence among "+setOfTags.size()+" unique tags");
		
	}
	
	public void write()
	{
		System.out.println();
		wf = new WriteFile(correctCorpus.substring(0, correctCorpus.length()-4)+"_pos_results.txt");
		try {
			wf.write(results);
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


