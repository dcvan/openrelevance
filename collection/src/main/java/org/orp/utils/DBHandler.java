package org.orp.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	public Set<Map<String, Object>> selectAll(String tabName){
		Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
		try{
			 ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabName);
			 result = toResultMap(rs);
			 clean();
		}catch(SQLException se){
			se.printStackTrace();
		}
		return result;
	}
	
	public Set<Map<String, Object>> toResultMap(ResultSet rs) 
			throws SQLException{
		Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
		Map<String, Integer> schema = getSchema(rs);
		while(rs.next()){
			Map<String, Object> row = new HashMap<String, Object>();
			for(String col : schema.keySet()){
				int type = schema.get(col);
				if(type == Types.VARCHAR
						|| type == Types.CHAR
						|| type == Types.LONGNVARCHAR
						|| type == Types.LONGVARCHAR)
					row.put(col, rs.getString(col));
				else if(type == Types.INTEGER
						|| type == Types.BIGINT)
					row.put(col, rs.getInt(col));
				else if(type == Types.FLOAT)
					row.put(col, rs.getFloat(col));
				else if(type == Types.DOUBLE
						|| type == Types.DECIMAL)
					row.put(col, rs.getDouble(col));
				else if(type == Types.DATE)
					row.put(col, rs.getDate(col));
				else{
					row = null;
					throw new SQLException("Unsupported type: " + type);
				}
			}
			result.add(row);
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
