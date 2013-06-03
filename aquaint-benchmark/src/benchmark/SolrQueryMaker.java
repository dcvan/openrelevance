package benchmark;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class SolrQueryMaker {
	
	private SolrServer solr;
	private String resultsFile;
	
	public SolrQueryMaker(SolrServer solr, String resultsFile){
		this.solr = solr;
		this.resultsFile = resultsFile;
	}
	
	public SolrDocumentList query(int tid, Map<Integer, Topic> topMap) 
			throws SolrServerException, FileNotFoundException{
		String query = topMap.get(tid).getTitle();
		SolrQuery params = new SolrQuery();
		params.setFields("docno", "score");
		params.setQuery("all:" + query);
		params.setRows(12000);
		QueryResponse response = solr.query(params);
		if(response == null) 
			throw new RuntimeException("Shouldn't be null");
		return response.getResults();
	}
	
	public void writeResults(Set<Integer> tidSet, Map<Integer,Topic> topMap)
			throws SolrServerException, FileNotFoundException{
		PrintWriter writer = new PrintWriter(resultsFile);
		for(Integer id : tidSet){
			SolrDocumentList docList = query(id, topMap);
			Result[] results = new Result[docList.size()];
			for(int i = 0; i < docList.size(); i ++){
				String docno = (String)docList.get(i).getFieldValue("docno");
				Float score = (Float)docList.get(i).getFieldValue("score");
				results[i] = new Result(docno, score);
			}
			
			Arrays.sort(results, new Comparator<Result>(){

				public int compare(Result arg0, Result arg1) {
					if(arg0.getScore() > arg1.getScore()) return -1;
					else return 1;
				}
			});
			
			for(int i = 0; i < results.length; i ++){
				writer.printf("%d\t%s\t%s\t%d\t%f\t%s\n", id, "Q0", results[i].getDocno(), i, results[i].getScore(), "STANDARD" );
			}
		}
		
		writer.close();
	}

class Result{
		private String docno;
		private float score;
		public Result(String docno, float score){
			this.docno = docno;
			this.score = score;
		}
		public String getDocno() {
			return docno;
		}
		public float getScore() {
			return score;
		}
	}
}
