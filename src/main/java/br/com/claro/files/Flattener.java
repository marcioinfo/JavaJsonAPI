package br.com.claro.files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;


public class Flattener {

	private Integer count = 0;
	
	private List<String> listAttributesPath = new ArrayList<String>();
	private TreeMap<Integer, Entry<String, JsonValue>> listAttributesToWriteOnTheFile = new TreeMap<Integer, Entry<String, JsonValue>>();
	private List<String> listAttributesToBuild = new ArrayList<String>();
	private List<String> listStringComparationPath = new ArrayList<String>();

	

	public void parseJsonObject(JsonObject jsonObject, String[] listAttributes) {
		this.listAttributesPath = Arrays.asList(listAttributes);
		this.flatten(jsonObject);
		System.out.println("");
	}
	
	
	private void flatten(JsonObject jsonObject) {
		Iterator<Entry<String, JsonValue>> iterator = this.bringIterator(jsonObject);
		
		
        while (iterator.hasNext()) {
        	
        	Entry<String, JsonValue> json = this.putOnVariables(iterator);
        	
        	if(json.getValue() instanceof JsonObject) {
        		this.flatten(json.getValue().asJsonObject());
        	} else if (json.getValue() instanceof JsonArray) {
        		this.arrayFlaten(json.getValue().asJsonArray());
        	}
        }
        
        
        
    }

	private void arrayFlaten(JsonArray arrayListValues) {
		for(Integer i = 0; i < arrayListValues.size(); i++) {
			
			if(arrayListValues.get(i) instanceof JsonObject) {
				this.flatten(arrayListValues.get(i).asJsonObject());
			} else if(arrayListValues.get(i) instanceof JsonArray) {
				this.arrayFlaten(arrayListValues.get(i).asJsonArray());
			}
			
		}	
		
	}

	
	private Entry<String, JsonValue> putOnVariables(Iterator<Entry<String, JsonValue>> iterator) {
		Entry<String, JsonValue> json = iterator.next();
        
		if(verifyIfContainsInListAttributes(json)) {
    	
    		listAttributesToBuild.add(json.getKey());
    		
    		forConstructNewString(json.getKey());
    	} 
		
    	
    	return json;
	}

	private Boolean verifyIfExistsInListAttributes(Entry<String, JsonValue> jsonValue) {
		for(String attribute : listAttributesPath) {
			if(jsonValue.getKey().equals(attribute)) {
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	private Boolean verifyIfContainsInListAttributes(Entry<String, JsonValue> jsonValue) {
		for(String attribute : listAttributesPath) {
			if(attribute.contains(jsonValue.getKey())) {
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	private Iterator<Entry<String, JsonValue>> bringIterator(JsonObject jsonObject) {
		Set<Entry<String, JsonValue>> entrySet = jsonObject.entrySet();
		return entrySet.iterator();
	}
	
	
	
	
	private void forConstructNewString(String key) {
		if(listStringComparationPath.size() == 0) {
			listStringComparationPath.add(key);
		} else {
			String stringTobeUsed = "";
			Boolean inserir = Boolean.TRUE;
			for(String stringComparationPath : listStringComparationPath) {
				for(String attributesPath : listAttributesPath) {
					stringTobeUsed = stringComparationPath + "." + key;
					if(attributesPath.contains(stringTobeUsed)) {
						inserir = Boolean.TRUE;
					}
				}
				
				if(inserir) {
					listStringComparationPath.add(stringTobeUsed);
				}
			}
		}	
	}
	
}