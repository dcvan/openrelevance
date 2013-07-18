package org.orp.server;


import java.sql.Connection;
import java.sql.DriverManager;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.orp.commons.CollectionResource;

public class CollectionServerResource extends WadlServerResource implements CollectionResource{

	private Connection c;
	
	@Override
	public void doInit(){
		try{
			c = DriverManager.getConnection("jdbc:sqlite:db/collection.db");
//			DatabaseMetaData dbm = c.getMetaData();
//			ResultSet rs = dbm.getTables(null, null, "collection", null);
//			if(rs.next())
//				System.out.println("Got it");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Representation present(){
		return null;
	}

	public void store(Representation entity) {
		
	}
	
}
