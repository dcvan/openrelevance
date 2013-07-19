package org.orp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.restlet.ext.json.JsonRepresentation;

public class JsonUtils {
	public static JsonRepresentation message(String content){
		Map<String, Object> msg = new HashMap<String, Object>();
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		msg.put("time", pattern.format(curDate));
		msg.put("message", content);
		
		return new JsonRepresentation(msg);
			
	}
	
}
