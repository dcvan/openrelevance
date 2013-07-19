package org.orp.servers;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.json.JSONObject;
import org.orp.commons.CollectionResource;
import org.orp.utils.JsonUtils;

public class CollectionServerResource extends WadlServerResource implements CollectionResource{

	private Connection c;
	private Statement stmt;
	private String id;
	private String name;
	
	@Override
	public void doInit(){
		try{
			c = DriverManager.getConnection("jdbc:sqlite:db/collection.db");
			stmt = c.createStatement();
			id = getRequest().getResourceRef().getIdentifier()
					.replaceAll("http://.*/collections/", "");
			ResultSet rs = stmt.executeQuery("SELECT * FROM COLLECTION WHERE ID='" + id + "'");
			while(rs.next()){
				id = rs.getString("id");
				name = rs.getString("name");
			}
			
			if(id == null)
				setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Representation present(){
		
		JSONObject info = new JSONObject();
		try{
			info.put("id", id);
			info.put("name", name);
			info.put("topics", getRequest().getResourceRef().getIdentifier()+ "/topics");
			info.put("qrels", getRequest().getResourceRef().getIdentifier()+ "/qrels");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new JsonRepresentation(info);
	}

	public Representation store(Representation entity) {
		try{	
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024000);
			RestletFileUpload upload = new RestletFileUpload(factory);
			List<FileItem> items = upload.parseRepresentation(entity);
			FileItem fileItem = null;
			StringBuilder repoPath = new StringBuilder("collections/").append(id);
			Map<String, String> map = new HashMap<String, String>();
			for(FileItem fi : items){
				map.put(fi.getFieldName(), fi.getString());
				if(fi.getFieldName().equals("upload"))
					fileItem = fi;
			}
			if(map.get("type").equals("topics"))
				repoPath.append("/topics/");
			else if(map.get("type").equals("qrels"))
				repoPath.append("/qrels/");
			else{
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				JsonUtils.message("Problematic request");
			}
			File finalFile = new File(repoPath.append(fileItem.getName()).toString());
			fileItem.write(finalFile);
		}catch(FileUploadException fue){
			fue.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return JsonUtils.message("Collection uploaded successfully.");
	}
	
}
