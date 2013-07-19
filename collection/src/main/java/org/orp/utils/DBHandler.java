package org.orp.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class DBHandler {
	Connection c;
	Statement stmt;
	
	private DBHandler(Connection c){ 
		this.c = c;
		try{
			stmt = c.createStatement();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	public static DBHandler newHandler(Connection c){
		return new DBHandler(c);
	}
	
	public Map<String, Object> selectAll(String tabName){
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			 ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabName);
			 result = toResultMap(rs);
			 clean();
		}catch(SQLException se){
			se.printStackTrace();
		}
		return result;
	}
	
	public Map<String, Object> toResultMap(ResultSet rs) 
			throws SQLException{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Integer> schema = getSchema(rs);
		while(rs.next()){
			for(String col : schema.keySet()){
				int type = schema.get(col);
				if(type == Types.VARCHAR
						|| type == Types.CHAR
						|| type == Types.LONGNVARCHAR
						|| type == Types.LONGVARCHAR)
					result.put(col, rs.getString(col));
				else if(type == Types.INTEGER
						|| type == Types.BIGINT)
					result.put(col, rs.getInt(col));
				else if(type == Types.FLOAT)
					result.put(col, rs.getFloat(col));
				else if(type == Types.DOUBLE
						|| type == Types.DECIMAL)
					result.put(col, rs.getDouble(col));
				else if(type == Types.DATE)
					result.put(col, rs.getDate(col));
				else{
					result = null;
					throw new SQLException("Unsupported type: " + type);
				}
			}
		}
		
		return result;
	} 
	
	public Map<String, Integer> getSchema(ResultSet rs) 
			throws SQLException{
		 ResultSetMetaData rsinfo = rs.getMetaData();
		 Map<String, Integer> schema = new HashMap<String, Integer>();
		 for(int i = 1; i <= rsinfo.getColumnCount(); i ++)
			 schema.put(rsinfo.getColumnName(i).toLowerCase(), rsinfo.getColumnType(i));
		 return schema;
	}
	
	private void clean() 
			throws SQLException{
		stmt.close();
		c.close();
	}
	
	
}
