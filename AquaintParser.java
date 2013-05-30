package solrbean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.LogManager;
import org.apache.log4j.varia.NullAppender;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrServer;


public class AquaintParser {

	public static void main(String[] args) {
		try{
			//Suppress log4j Warnings
			System.setProperty("log4j.defaultInitOverride","true");
			LogManager.resetConfiguration();
			LogManager.getRootLogger().addAppender(new NullAppender());
			
			//Parse AQUAINT data in XML format and convert them into JavaBeans
			String filePath = "/home/dc/Openrel/data/aquaint/data.txt";
			List<String> docs = splitDocuments(filePath);
			List<AquaintBean> abs = toBeans(docs);
			
			//Connect Solr server and import the list of JavaBeans into Solr
			SolrServer solr = new HttpSolrServer("http://localhost:9090");
			solr.addBeans(abs);
			solr.commit();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public static List<String> splitDocuments(String path)
		throws IOException{
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			List<String> docs = new ArrayList<String>();
			
			//Reference AQUAINT DTD
			String header = "<?xml version=\"1.0\"?>\n"
							+"<!DOCTYPE DOC SYSTEM \"aquaint.dtd\">\n";
			String doc = new String(header);
			boolean isStart = false;
			
			while(br.ready()){
				String line = br.readLine();
				if(line.equals("<DOC>")) isStart = true;
				else if(line.equals("</DOC>")){
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
	
	public static List<AquaintBean> toBeans(List<String> docs) 
			throws UnsupportedEncodingException, XMLStreamException, ParseException{
		List<AquaintBean> beans = new ArrayList<AquaintBean>();
		Iterator<String> it = docs.iterator();
		XMLInputFactory xif = XMLInputFactory.newInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		while(it.hasNext()){
			String doc = it.next();
			XMLEventReader xer = xif.createXMLEventReader(
					new ByteArrayInputStream(doc.getBytes("UTF-8")));
			AquaintBean abean = new AquaintBean();
		
			while(xer.hasNext()){
				XMLEvent event = xer.nextEvent();
				if(event.isStartElement()){
					StartElement se = event.asStartElement();
					if(se.getName().getLocalPart().equals("DOCNO")){
						event = xer.nextEvent();
						abean.setDocno(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("DOCTYPE")){
						event = xer.nextEvent();
						if(event.isCharacters())
						abean.setDoctype(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("DATE_TIME")){
						event = xer.nextEvent();
						Date date = (Date)format.parse(event.asCharacters().getData());
						abean.setDatetime(date);
					}
					if(se.getName().getLocalPart().equals("HEADER")){
						event = xer.nextEvent();
						if(event.isCharacters())
						abean.setHeader(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("SLUG")){
						event = xer.nextEvent();
						if(event.isCharacters())
						abean.setSlug(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("HEADLINE")){
						event = xer.nextEvent();
//						if(event.isCharacters())
						abean.setHeadline(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("TEXT")){
						event = xer.nextEvent();
						if(event.isCharacters())
						abean.setText(event.asCharacters().getData().trim());
					}
					if(se.getName().getLocalPart().equals("TRAILER")){
						event = xer.nextEvent();
						if(event.isCharacters())
						abean.setTrailer(event.asCharacters().getData().trim());
					}
				}
				else if(event.isEndElement()){
					EndElement ee = event.asEndElement();
					if(ee.getName().getLocalPart().equals("DOC"))
						beans.add(abean);
				}
			}
		}
		
		return beans;
	}

}

class AquaintBean{
	
	@Field("docno")
	String docno;
	
	@Field("doctype")
	String doctype;
	
	@Field("datetime")
	Date datetime;
	
	@Field("header")
	String header;
	
	@Field("slug")
	String slug;
	
	@Field("headline")
	String headline;
	
	@Field("text")
	String text;
	
	@Field("trailer")
	String trailer;
	
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		this.docno = docno;
	}
	public String getDoctype() {
		return doctype;
	}
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	
}

