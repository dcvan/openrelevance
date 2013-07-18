package org.orp.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import org.orp.commons.CollectionsResource;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class CollectionsServerResource extends WadlServerResource implements CollectionsResource{
	
	private Connection c;
	private Statement stmt;
	private static final String REPO = "collections/";
	
	public void doInit(){
		try{
			c = DriverManager.getConnection("jdbc:sqlite:db/collection.db");
			stmt = c.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Representation list(){
		StringBuilder result = new StringBuilder();
		try{
			ResultSet rs = stmt.executeQuery("SELECT * FROM COLLECTION");
			for(int i = 1; rs.next(); i ++){
				result.append("C" + String.valueOf(i) + ":{\n");
				result.append("    ID:" + rs.getString("id") + "\n");
				result.append("    Name:" + rs.getString("name") + "\n");
				result.append("    URI:" + rs.getString("uri") + "\n");
				result.append("}\n");
			}
			rs.close();
			stmt.close();
			c.close();
			
			if(result.length() == 0)
				return new StringRepresentation("No collection available now.\n");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new StringRepresentation(result);
	}

	public Representation create(JsonRepresentation entity) {
		StringBuilder summary = new StringBuilder();
		try {
			String colName = entity.getJsonObject().getString("name");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			String uri = getRequest().getResourceRef().getIdentifier() + "/" + id;
			
			new File(REPO + id + "/topics/").mkdirs();
			new File(REPO + id + "/qrels/").mkdirs();
			
			stmt.executeUpdate(
					"INSERT INTO COLLECTION VALUES('" + id + "','" + colName + "','" + uri + "')");
			stmt.close();
			c.close();
			
			summary.append("ID:" + id + "\n");
			summary.append("Name:" + colName + "\n");
			summary.append("URI:" + uri + "\n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new StringRepresentation(summary);
	}
	
	public Representation delete(JsonRepresentation entity){
		StringRepresentation summary = null;
		try{
			String id = entity.getJsonObject().getString("id");
			new File(REPO + id + "/").delete();
			stmt.execute("DELETE FROM COLLECTION WHERE ID='" + id + "'");
			stmt.close();
			c.close();
			
			summary = new StringRepresentation("Collection " + id + " has been deleted");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return summary;
	}
	
}
