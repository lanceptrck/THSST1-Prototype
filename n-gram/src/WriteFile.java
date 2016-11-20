import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WriteFile {
	
private String path;
private ArrayList<String> data;
	
	public WriteFile(String path){
		this.path = path;
		data = new ArrayList<String>();
	}
	
	public void write(ArrayList<String> data) throws IOException{
		
	this.data = data;	
		
	PrintWriter writer = new PrintWriter(path, "UTF-8");
	
	for(int i = 0; i<data.size(); i++)
		writer.println(data.get(i));
	
	writer.close();
		
	}

}
