package org.orp.application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.orp.server.CollectionServerResource;
import org.orp.server.CollectionsServerResource;
import org.restlet.Restlet;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;

public class CollectionApplication extends WadlApplication{
	public CollectionApplication(){
		Connection c = null;
		Statement stmt = null;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:db/collection.db");
			stmt = c.createStatement();
			DatabaseMetaData dbm = c.getMetaData();
			ResultSet rs = dbm.getTables(null, null, "collection", null);
			if(!rs.next()){
				stmt.executeUpdate("CREATE TABLE COLLECTION(" +
						"ID VARCHAR(20) PRIMARY KEY NOT NULL," +
						"NAME VARCHAR(20) NOT NULL," +
						"URI VARCHAR(50) NOT NULL)");
			}
			
			stmt.close();
			c.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Restlet createInboundRoot(){
		Router router = new Router();
		router.attach("/collections", CollectionsServerResource.class);
		router.attach("/collections/{colId}", CollectionServerResource.class);
		return router;
	}
}
