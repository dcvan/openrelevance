package benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrecEvaluator {
	private String qrels;
	private String results;
	
	public TrecEvaluator(String pathToQrels, String pathToResults){
		qrels = pathToQrels;
		results = pathToResults;
	}
	
	public String evaluateAll(String metric) 
			throws IOException, InterruptedException{
		String trecEval = findTrecEval();
		if(trecEval.isEmpty())
			throw new RuntimeException("No trec_eval installation found!");
		else{
			Process p = Runtime.getRuntime().exec(trecEval + " -a " + qrels + " " + results);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String results = "";
			while(br.ready()){
				String line = br.readLine();
				String[] fields = line.split("[ \t]+");
				if(fields[0].equals(metric))
					results = metric + ": " + fields[2];
			}
			br.close();
			return results;
			}
	}
	
	public String evaluateQuery(int tid, String metric) 
			throws IOException, InterruptedException{
		String trecEval = findTrecEval();
		if(trecEval.isEmpty())
			throw new RuntimeException("No trec_eval installation found!");
		else{
			Process p = Runtime.getRuntime().exec(trecEval + " -q " + qrels + " " + results);
			p.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String results = "";
			while(br.ready()){
				String line = br.readLine();
				String[] fields = line.split("[ \t]+");
				if(fields[0].equals(metric) && fields[1].equals(String.valueOf(tid)))
					results = fields[0] + "(" + fields[1] + "): " + fields[2];
			}
			br.close();
			return results;
		}
	}
	
	public String findTrecEval() 
			throws IOException, InterruptedException{
		Process p = Runtime.getRuntime().exec("locate trec_eval");
		p.waitFor();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String trecEval = "";
		while(br.ready()){
			String line = br.readLine();
			if(new File(line).isFile() && line.endsWith("trec_eval")){
				trecEval = line;
				break;
			}
		}
		return trecEval;
	}
}
