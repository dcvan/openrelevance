package benchmark;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
		params.set("defType", "edismax");
		params.set("q", query);
		params.set("qf", "body^10 text^10");
		params.set("sort", "score desc");
		
		params.setRows(14000);
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
			for(int i = 0; i < docList.size(); i ++){
				String docno = (String)docList.get(i).getFieldValue("docno");
				Float score = (Float)docList.get(i).getFieldValue("score");
				writer.printf("%d\t%s\t%s\t%d\t%f\t%s\n", id, "Q0", docno, i, score, "STANDARD" );
			}
		}
		
		writer.close();
	}
}
