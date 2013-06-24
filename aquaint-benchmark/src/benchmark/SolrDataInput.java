package benchmark;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;


public class SolrDataInput {
	
	private SolrServer solr;
	private String dataPath;
	private DateFormat format;
	
	public SolrDataInput(SolrServer solr, String dataPath){
		this.solr = solr;
		this.dataPath = dataPath;
		setDateFormat("");
	}
	
	/**
	 * Index documents in one or many data source files in Solr
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws XMLStreamException
	 */
	public void indexData()
			throws IOException, SolrServerException, XMLStreamException{
		File file = new File(dataPath);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f : files){
				format = getDateFormat(f.getName());
				solr.addBeans(getDocuments(f));
				solr.commit();
			}
		}
		else{
			format = getDateFormat(file.getName());
			solr.addBeans(getDocuments(file));
			solr.commit();
		}
	}
	
	/**
	 * 
	 * @param name of the data source file
	 * @return the particular date format for each data source
	 */
	
	public DateFormat getDateFormat(String source){
		if(source.contains("APW"))
			setDateFormat("yyyy-MM-dd hh:mm:ss");
		else if(source.contains("NYT"))
			setDateFormat("yyyy-MM-dd hh:mm");
		else if(source.contains("XIN"))
			setDateFormat("yyyy-MM-dd");
			
		return format;
	}
	
	/**
	 * 
	 * @param A file contains one or many AQUAINT documents
	 * @return A list of AQUAINT documents
	 * @throws IOException
	 * @throws XMLStreamException 
	 */
	public List<AquaintDocument> getDocuments(File file)
			throws IOException, XMLStreamException{
				List<AquaintDocument> aDocList = new ArrayList<AquaintDocument>();
				String header = "<?xml version=\"1.0\"?>\n"
						+"<!DOCTYPE DOC SYSTEM \"aquaint.dtd\">\n";
				List<String> docList = ParseUtils.splitDocuments(file, header, "DOC");
				for(String doc : docList)
					aDocList.add(getDocument(doc));
				
				return aDocList;
		}
	/**
	 * 
	 * @param A document in XML stored in a String
	 * @return An AQUAINT document
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException if the document is not 
	 * in XML format or is not well-formed
	 */
	public AquaintDocument getDocument(String xmlDoc) 
			throws UnsupportedEncodingException, XMLStreamException{
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLEventReader xer = xif.createXMLEventReader(
				new ByteArrayInputStream(xmlDoc.getBytes("UTF-8")));																															 
		AquaintDocument adoc = new AquaintDocument();
		StringBuilder text = new StringBuilder();
		
		while(xer.hasNext()){
			XMLEvent event = xer.nextEvent();			
			if(event.isStartElement()){
				StartElement se = event.asStartElement();
				if(se.getName().getLocalPart().equals("DOCNO")){
					event = xer.nextEvent();
					adoc.setDocno(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("DOCTYPE")){
					event = xer.nextEvent();
					if(event.isCharacters())
					adoc.setDoctype(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("TXTTYPE")){
					event = xer.nextEvent();
					if(event.isCharacters())
					adoc.setTxttype(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("DATE_TIME")){
					event = xer.nextEvent();
					try{
						Date date = format.parse(event.asCharacters().getData());
						adoc.setDatetime(date);
					}catch(ParseException ex){
						adoc.setDatetime(null);
					}
				}
				if(se.getName().getLocalPart().equals("HEADER")){
					event = xer.nextEvent();
					if(event.isCharacters())
					adoc.setHeader(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("SLUG")){
					event = xer.nextEvent();
					if(event.isCharacters())
					adoc.setSlug(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("CATEGORY")){
					event = xer.nextEvent();
					if(event.isCharacters())
					adoc.setCategory(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("HEADLINE")){
					event = xer.nextEvent();
					if(event.isCharacters())
						adoc.setHeadline(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("TEXT")
						|| se.getName().getLocalPart().equals("P")){
					event = xer.nextEvent();
					if(event.isCharacters())
						text.append(event.asCharacters().getData().trim()).append("\n");
					if(event.isStartElement()){
						StartElement tse = event.asStartElement();
						if(tse.getName().getLocalPart().equals("P")){
							event = xer.nextEvent();
							if(event.isCharacters())
								text.append(event.asCharacters().getData().trim()).append("\n");
						}
					}
				}
				if(se.getName().getLocalPart().equals("SUBHEAD")){
					event = xer.nextEvent();
					if(event.isCharacters())
						adoc.setSubhead(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("ANNOTATION")){
					event = xer.nextEvent();
					if(event.isCharacters())
						adoc.setAnnotation(event.asCharacters().getData().trim());
				}
				if(se.getName().getLocalPart().equals("TRAILER")){
					event = xer.nextEvent();
					if(event.isCharacters())
						adoc.setTrailer(event.asCharacters().getData().trim());
				}
			} 
			else if(event.isEndElement()){
				EndElement ee = event.asEndElement();
				if(ee.getName().getLocalPart().equals("TEXT")){
					adoc.setText(text.toString());
					text = new StringBuilder();
				}
			}
		}
		
		return adoc;
	}
	
	private void setDateFormat(String f){
		format = new SimpleDateFormat(f);
	}

}
