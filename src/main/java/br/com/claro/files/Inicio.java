package br.com.claro.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Inicio {
	
	public static String[] verificarColuna(String arg) {
		
		switch (arg) {   
		
		case "FORA_TOA_01":
			return ColunasJson.FORA_TOA_01;
			
		case "FORA_TOA_02":
			return ColunasJson.FORA_TOA_02;
			
		case "EVENTOS_DE_MENSAGENS_01":
			return ColunasJson.EVENTOS_DE_MENSAGENS_01;
		 
		 case "EVENTOS_DE_MENSAGENS_03":
			return ColunasJson.EVENTOS_DE_MENSAGENS_03;
			
		 case "RE_ROUTER":
			return ColunasJson.RE_ROUTER;
			
			
			
		}
 	return null;
	}	
	
	public static void main(String[] args) throws Exception {
		args = new String[1];
		//args[0] = "FORA_TOA_01";//apenas para testar os arquivos
		
		
		for(String arg : args) {
			String[] atributoEscolhido = verificarColuna(arg);
			
			if(atributoEscolhido == null ) {
				throw new Exception("Erro");
			}
			
						
			for(Field field : ColunasJson.class.getDeclaredFields()) {
				if(field.getName().equals(arg)) {
				//	List<Linha> l = JSONFlattener.parseJson("C:\\Users\\614952\\Desktop\\Projeto Claro\\" + arg +".json", atributoEscolhido);
					//System.out.println(JSONFlattener.dataBaseFormat(l,new File("C:\\Users\\614952\\Desktop\\Projeto Claro\\" + arg + ".txt")));
					 
				}
				
			}	
			
		}
	}

}