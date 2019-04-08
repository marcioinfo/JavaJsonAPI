package br.com.claro.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonReader;
import javax.json.Json;
import javax.json.JsonObject;

public class JsonTransformer {
	
	private static final Logger LOGGER = Logger.getLogger(JSONFlattener.class.getName());

	public JsonObject parseJsonFromFile(String fileString) throws FileNotFoundException {
		
		LOGGER.log(Level.INFO, "Lendo Arquivo Json: " + fileString);
    	InputStream is = new FileInputStream(new File(fileString));
    	 
            JsonReader reader = Json.createReader(is);

        LOGGER.log(Level.INFO, "Criando objeto json: " + fileString);
            JsonObject JsonObject = reader.readObject();
        
        return JsonObject;
	}

}
