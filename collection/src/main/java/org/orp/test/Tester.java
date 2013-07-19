package org.orp.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.orp.utils.JsonUtils;

public class Tester {

	public static void main(String[] args) throws JSONException, IOException {
		File file = new File("collections/hello.txt");
//		System.out.println(file.getAbsolutePath());
//		System.out.println(CollectionUtils.message("Test Test 123").getText());
		
		Map<String, Object> bob = new HashMap<String, Object>();
		Map<String, Object> kevin = new HashMap<String, Object>();
		
		bob.put("name", "bob");
		bob.put("address", "nc");
		bob.put("gender", "male");
		
		kevin.put("name", "kevin");
		kevin.put("address", "wa");
		kevin.put("gender", "female");
		
		Map<String, Object> users = new HashMap<String, Object>();
		users.put("user1", bob);
		users.put("user2", kevin);
		
		Set<String> breacher = new HashSet<String>();
		users.put("user3", breacher);
		
		Tester test = new Tester();
		users.put("user4", test);
		
		
	}
	
	private static void delete(String name){
		File file = new File(name);
		if(file.isDirectory()){
			System.out.println("It is not a directory");
			if(file.list().length == 0){ 
				file.delete();
			}else{
				String[] names = file.list();
				for(String n : names) delete(n);
			}
		} else {
			file.delete();
			System.out.println("It is a directory");
		}
	}

}
