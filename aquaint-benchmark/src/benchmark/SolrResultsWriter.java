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

public class SolrResultsWriter {
	
	private SolrServer solr;
	private String resultsFile;
	
	public SolrResultsWriter(SolrServer solr, String resultsFile){
		this.solr = solr;
		this.resultsFile = resultsFile;
	}
	
	@SuppressWarnings("resource")
	public void writeResults(Set<Integer> tidSet, Map<Integer,Topic> topMap)
			throws SolrServerException, FileNotFoundException{
		PrintWriter writer = new PrintWriter(resultsFile);
		for(Integer id : tidSet){
			String topTitle = topMap.get(id).getTitle();
			SolrQuery params = new SolrQuery();
			params.setFields("docno", "score");
			params.setQuery(topTitle);
			QueryResponse response = solr.query(params);
			if(response == null) 
				throw new RuntimeException("Shouldn't be null");
			SolrDocumentList docList = response.getResults();
			Result[] results = new Result[docList.size()];
			for(int i = 0; i < docList.size(); i ++){
				String docno = (String)docList.get(i).getFieldValue("docno");
				Float score = (Float)docList.get(i).getFieldValue("score");
				results[i] = new Result(docno, score);
			}
			Arrays.sort(results, new ResultComparator());
			for(int i = 0; i < results.length; i ++){
				writer.printf("%d\t%s\t%s\t%d\t%f\t%s\n", id, "Q0", results[i].getDocno(), i + 1, results[i].getScore(), "STANDARD" );
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

class ResultComparator implements Comparator<Result>{

		@Override
		public int compare(Result o1, Result o2) {
			if(o1.getScore() > o2.getScore()) return -1;
			else return 1;
		}
		
	}
}
