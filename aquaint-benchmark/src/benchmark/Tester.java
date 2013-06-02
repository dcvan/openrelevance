package benchmark;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.varia.NullAppender;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Tester {

	public static void main(String[] args){
		try{
			//Suppress log4j Warnings
			System.setProperty("log4j.defaultInitOverride","true");
			LogManager.resetConfiguration();
			LogManager.getRootLogger().addAppender(new NullAppender());
			
			SolrServer solr = new HttpSolrServer("http://localhost:9090");
			SolrQuery query = new SolrQuery();
//			query.setQuery("Boston");
//			query.setFields("docno", "score");
//			QueryResponse response = solr.query(query);
//			SolrDocumentList docList = response.getResults();
//			for(SolrDocument d : docList){
//				System.out.println(d.getFieldValue("docno"));
//				System.out.println(d.getFieldValue("score"));
//			}
			
			TopicParser tp = new TopicParser("/home/dc/Openrel/data/aquaint/evaluation/");
//			Map<Integer, Topic> map = tp.getTopics();
//			for(Integer s : map.keySet())
//				if(s.equals(70)) System.out.println(s);
		
			Set<Integer> tids = ParseUtils.parseQrel("/home/dc/Openrel/data/aquaint/qrels.txt");
			SolrResultsWriter srw = new SolrResultsWriter(solr, "/home/dc/Openrel/data/aquaint/results.txt");
			srw.writeResults(tids, tp.getTopics());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
