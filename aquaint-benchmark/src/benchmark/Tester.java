package benchmark;

import java.io.File;
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
//			SolrDataInput sdi = new SolrDataInput(solr, "data/data.txt");
//			TopicParser tp = new TopicParser("data/evaluation/");
//			Map<Integer, Topic> map = tp.getTopics();
		
//			Set<Integer> tids = ParseUtils.parseQrel("data/qrels.txt");
//			SolrQueryMaker srw = new SolrQueryMaker(solr, "data/results.txt");
//			srw.writeResults(tids, tp.getTopics());
			
			String qrels = "data/qrels.txt";
			String results = "data/results.txt";
			TrecEvaluator evaluator = new TrecEvaluator(qrels, results);
			System.out.println(evaluator.evaluateAll("P10"));
			System.out.println(evaluator.evaluateQuery(69, "P10"));
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
