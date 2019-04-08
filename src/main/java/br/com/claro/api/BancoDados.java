package br.com.claro.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Elison Correa
 */
public class BancoDados {
    
    private Map<String, Arquivo> arquivos;
    
    public BancoDados() {
        this.arquivos = new HashMap<>();
    }
    
    private void getDados() {
        try {
            FileReader arq = new FileReader("dados.dat");
            BufferedReader buffer = new BufferedReader(arq);
            
            String command = buffer.readLine();
            while(command != null) {
                String[] dados = command.split(":");
                
                Arquivo a = new Arquivo();
                a.setArquivo(dados[0]);
                a.setPagina(("null".equals(dados[1]) ? null : dados[1]));
                
                arquivos.put(dados[0], a);
                
                command = buffer.readLine();
            }
            
            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void gravarDados() {
        try {
            
            StringBuilder builder = new StringBuilder();
            for (Arquivo a : arquivos.values()) {
                builder.append(a.getArquivo()).append(":").append(a.getPagina()).append(System.lineSeparator());
            }
            
            FileWriter arq = new FileWriter("dados.dat", false);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.printf(Locale.US, builder.toString());
            arq.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addDados(String arquivo, String pagina) {
        try {
            this.getDados();
            
            Arquivo a = new Arquivo();
            a.setArquivo(arquivo);
            a.setPagina(pagina);
            arquivos.put(arquivo, a);
            
            this.gravarDados();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getPagina(String arquivo) {
        String pagina = null;
        try {
            this.getDados();
            
            if (arquivos.containsKey(arquivo)) {
                Arquivo a = arquivos.get(arquivo);
                pagina = a.getPagina();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagina;
    }
    
    public boolean has(String arquivo) {
        boolean has = false;
        try {
            this.getDados();
            
            has = arquivos.containsKey(arquivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }
    
}