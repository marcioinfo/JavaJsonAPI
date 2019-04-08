package br.com.claro.files;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class KeyPrefixObj {
	
	
	
	private String key;
	private String prefix;
	private JsonObject obj;
	private JsonArray array;
	
	public KeyPrefixObj(String key, String prefix, JsonObject obj) {
		super();
		this.key = key;
		this.prefix = prefix;
		this.obj = obj;
	}
	public KeyPrefixObj(String key, String prefix) {
		super();
		this.key = key;
		this.prefix = prefix;
		this.obj = null;
	}
	public KeyPrefixObj(String key, String prefix, JsonArray array) {
		super();
		this.key = key;
		this.prefix = prefix;
		this.array = array;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public JsonObject getObj() {
		return obj;
	}
	public void setObj(JsonObject obj) {
		this.obj = obj;
	}
	
	public String getPath() {
		return  (prefix+key).replaceAll("([0-9\\[\\]])","");
//		return  (prefix+key).replaceAll("\\[(.*?)\\]", "");
	}
	@Override
	public String toString() {
		return  prefix+key;
	}
	

}