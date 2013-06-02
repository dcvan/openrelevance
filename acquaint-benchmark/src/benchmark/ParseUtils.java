package benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParseUtils {

	public static List<String> splitDocuments(File file, String rootName)
			throws IOException{
			return splitDocuments(file, "", rootName);
		}
	
	
	public static List<String> splitDocuments(File file, String header, String rootName)
			throws IOException{
				BufferedReader br = new BufferedReader(new FileReader(file));
				List<String> docs = new ArrayList<String>();
				String doc = new String(header);
				String startElement = "<" + rootName +">";
				String endElement = "</" + rootName + ">";
				boolean isStart = false;
				
				while(br.ready()){
					String line = br.readLine();
					if(line.equals(startElement)) isStart = true;
					else if(line.equals(endElement)){
						doc += line;
						isStart = false;
						docs.add(doc);
						doc = new String(header);
					}
					
					if(isStart)
						doc += line;
				}
				
				br.close();
				return docs;
		}
	
	public static Set<Integer> parseQrel(String qrelPath) 
			throws IOException{
		File qfile = new File(qrelPath);
		Set<Integer> tids = new HashSet<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(qfile));
	
		while(br.ready()){
			String line = br.readLine();
			String[] fields = line.split("[ \t]+");
			tids.add(Integer.parseInt(fields[0]));
		}
		
		br.close();
		return tids;
	}

}
