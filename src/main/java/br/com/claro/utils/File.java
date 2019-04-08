package br.com.claro.utils;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.claro.api.Config;
import br.com.claro.api.Event;

/**
*
* @author Elison Correa
*/
public class File {
    
	  
	
    public static void createFile(Event event, String fileName, boolean debug) throws IOException {
    	
    	Config config = ConfigUtils.get();
        java.io.File fileFolder = new java.io.File(config.getFilesPath());
        fileFolder.mkdirs();
        if (debug) {
            System.out.println("Escrevendo arquivo: " + config.getFilesPath()+"/" + fileName + ".txt");
        }
        FileWriter arq = new FileWriter(config.getFilesPath()+"/" + fileName + ".txt", false);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf(event.toString());
        arq.close();
    }
    

    public static void createFile(String content, String fileName, boolean debug) throws IOException {
    	Config config = ConfigUtils.get();
        java.io.File fileFolder = new java.io.File(config.getFilesPath());
        fileFolder.mkdirs();
        if (debug) {
            System.out.println("Escrevendo arquivo: " + config.getFilesPath()+"/" + fileName + ".json");
        }
          
       // OutputStreamWriter arq = new OutputStreamWriter(new FileOutputStream(config.getFilesPath()+"/" + fileName + ".json", false),"UTF-8");
        FileWriter arq = new FileWriter(config.getFilesPath()+"/" + fileName + ".json", false);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf(content);
        arq.close();
    }
    
    
    
    public static void createException(String content, String fileName, boolean debug) throws IOException {
    
		SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
		Calendar data = Calendar.getInstance();
		
    	Config config = ConfigUtils.get();
        java.io.File fileFolder = new java.io.File(config.getFilesPath());
        fileFolder.mkdirs();
        if (debug) {
            System.out.println("Escrevendo arquivo: " + config.getFilesPath()+"/" + "log_toa_api_" + formatoData
					.format(data
							.getTime())+".log");
        }
          
       // OutputStreamWriter arq = new OutputStreamWriter(new FileOutputStream(config.getFilesPath()+"/" + fileName + ".json", false),"UTF-8");
        FileWriter arq = new FileWriter(config.getFilesPath()+"/" + "log_toa_api_" + formatoData
				.format(data
						.getTime())+ ".log", false);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf(content);
        arq.close();
    }
    
}