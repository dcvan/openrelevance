package testplace;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLTester {
	public static void main(String[] args) {
		try{
			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLEventReader xer = xif.createXMLEventReader(new FileReader(new File("test.xml")));
			while(xer.hasNext()){
				XMLEvent event = xer.nextEvent();
				if(event.isStartElement()){
					StartElement se = event.asStartElement();
					if(se.getName().getLocalPart().equals("BODY")){
						event = xer.nextEvent();
						System.out.println(event.asCharacters().getData().trim());
					}	
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
