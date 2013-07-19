package org.orp.servers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.orp.commons.CollectionsResource;
import org.orp.utils.DBHandler;
import org.orp.utils.JsonUtils;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;

public class CollectionsServerResource extends WadlServerResource implements CollectionsResource{
	
	private Connection c;
	private DBHandler handler;
	private Statement stmt;
	private static final String REPO = "collections/";
	
	public void doInit(){
		try{
			c = DriverManager.getConnection("jdbc:sqlite:db/collection.db");
			handler = DBHandler.newHandler(c);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Representation list(){
		Map<String, Object> result = new HashMap<String, Object>();
		Set<Map<String, Object>> rs = handler.selectAll("COLLECTION");
		Iterator<Map<String, Object>> iter = rs.iterator();
		while(iter.hasNext()){
			int i = 1;
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.putAll(iter.next());
			result.put("c" + i ++, obj);
		}
			
		if(result.isEmpty())
			setStatus(Status.SUCCESS_NO_CONTENT);
			
		return new JsonRepresentation(result);
	}

	public Representation create(JsonRepresentation entity) {
		JSONObject summary = new JSONObject();
		try {
			String name = entity.getJsonObject().getString("name");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			String uri = getRequest().getResourceRef().getIdentifier() + "/" + id;
			
			new File(REPO + id + "/topics/").mkdirs();
			new File(REPO + id + "/qrels/").mkdirs();
			
			stmt.executeUpdate(
					"INSERT INTO COLLECTION VALUES('" + id + "','" + name + "','" + uri + "')");
			stmt.close();
			c.close();
			
			summary.put("id", id);
			summary.put("name", name);
			summary.put("uri", uri);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setStatus(Status.SUCCESS_CREATED);
		return new JsonRepresentation(summary);
	}
	
	public Representation delete(JsonRepresentation entity){
		try{
			String id = entity.getJsonObject().getString("id");
			ResultSet rs = stmt.executeQuery("SELECT * FROM COLLECTION WHERE ID='" + id + "'");
			if(!rs.next())
				return JsonUtils.message("Collection " + id + " not found.");
			delete(new File(REPO + id));
			stmt.execute("DELETE FROM COLLECTION WHERE ID='" + id + "'");
			stmt.close();
			c.close();
			
			String msg = "Collection " + id + " has been deleted.";
			return JsonUtils.message(msg);
		
		}catch(JSONException je){
			je.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void delete(File file){
		if(file.isDirectory()){
			if(file.list().length == 0){ 
				file.delete();
			}else{
				File[] files = file.listFiles();
				for(File f : files) delete(f);
				delete(file);
			}
		} else {
			file.delete();
		}
	}
}
