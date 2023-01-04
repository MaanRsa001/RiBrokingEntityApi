package com.maan.insurance.error;

import java.util.HashMap;
import java.util.Map;

public class ErrorInstance {
	
	private static Map<String,Error> map= new HashMap<String, Error>();
	
	public ErrorInstance(){
		
		if(map.size()==0) {new LoadError().load();};
	}
	public static Error get(String key){
		
		if(map.size()==0) {new LoadError().load();};
		Error error=map.get(key);
		return error==null?create(key):error;
	}
	
	public static void add(Error error,String key){
		map.put(key, error);
	}
	public static Error create(String key){
		Error error=new Error();
		if(key.indexOf("-")!=-1){
			String[] split = key.split("-");
			error.setCode("invalid_field");
			error.setField(split[0]);
			error.setMessage(split[1]);
		}
		return error;
	}
	
	
	
}
