import java.util.Iterator;

class NgramWordIterator implements Iterator<String> {

    String[] words;
    String sentence;
    int pos = 0, n;

    public NgramWordIterator(int n, String str) {
        this.n = n;
        this.sentence = str;
       // words = str.split(" ");
    }
    
    public boolean hasNext() {
        return pos < sentence.length() - n + 1;
    }

    public String next() {
        StringBuilder sb = new StringBuilder();
        for (int i = pos; i < pos + n; i++)
        	sb.append(sentence.charAt(i));
        pos++;
        return sb.toString();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}