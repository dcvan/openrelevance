package testplace;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLTester {
	public static void main(String[] args) {
		try{
			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLEventReader xer = xif.createXMLEventReader(new FileReader(new File("test.xml")));
			String text = new String();
			while(xer.hasNext()){
				XMLEvent event = xer.nextEvent();
				if(event.isStartElement()){
					StartElement se = event.asStartElement();
					if(se.getName().getLocalPart().equals("TEXT")
							||se.getName().getLocalPart().equals("P")){
						event = xer.nextEvent();
						if(event.isCharacters())
							text += event.asCharacters().getData().trim();
					}	
				}
				else if(event.isEndElement()){
					EndElement ee = event.asEndElement();
					if(ee.getName().getLocalPart().equals("TEXT"))
						System.out.println(text);
				
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
