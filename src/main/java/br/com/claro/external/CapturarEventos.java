package br.com.claro.external;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import br.com.claro.api.BancoDados;
import br.com.claro.api.Config;
import br.com.claro.json.JSONObject;
import br.com.claro.utils.Base64;
import br.com.claro.utils.ConfigUtils;
import br.com.claro.utils.File;

/**
 *
 * @author Elison Correa
 */
public class CapturarEventos {
    
    private boolean debug;
    private Config config;
    public CapturarEventos(boolean debug) {
        this.debug = debug;
        this.config = ConfigUtils.get();
    }
    
    /**
     * Retorna a lista de eventos de uma inscrição
     * @param subscription
     * @return
     * @throws Exception 
     */
    public String salvarEventos(String id, String page, boolean gerar, String nomeArquivo) throws Exception{
        
    	try {
    		
    		OkHttpClient client = new OkHttpClient();

            if (id == null || id.isEmpty()) {
                throw new Exception("Id inválido");
            } 
            
            String sPage = "";
            if (page != null) {
                if (page.isEmpty()) {
                    throw new Exception("Página inválida");
                }
                sPage = "&page="+page;
            }
            
            Request request = new Request.Builder()
              //.url(Credentials.END_POINT + "/events/?subscriptionId="+id+sPage) // Endpoint passando o subcription id e o next page
              .url(Config.getEndPoint() + "/events/?subscriptionId="+id+sPage )
              .get()
              .addHeader("Authorization", "Basic " + Base64.encodeBytes((config.getApiuser()+":"+config.getApipassword()).getBytes()))
              .addHeader("Accept", "application/json")
              .addHeader("Cache-Control", "no-cache")
              .build();

            Response  response = client.newCall(request).execute();
            if (!response.isSuccessful()) { // teste pelo sucesso da requisição
                
            	this.executarExcecao(response, null, null, nomeArquivo);
            }
            
            String result = response.body().string();
                                    
            JSONObject retorno = new JSONObject(result);
            
            if (retorno.has("status")) { // sempre que é retornoado o atributo Status significa que algo deu errado
            	
            	
            	this.executarExcecao(null, retorno, null, nomeArquivo);
            
            }
            String newPage = "";
            //if (retorno.has("found") && retorno.getBoolean("found")) {
                
                // atualizo o next page da subscription
                if (retorno.has("nextPage") && !retorno.isNull("nextPage")) {
                    newPage = retorno.getString("nextPage");
                }
            //}
            
            if (newPage != null) {
                BancoDados bancoDados = new BancoDados();
                bancoDados.addDados(nomeArquivo, newPage);
            }
            
            if (newPage != null && !newPage.isEmpty() && !newPage.equals(page)) {
                result += this.salvarEventos(id, newPage, gerar, nomeArquivo);
            }
            
            if (gerar) {
            	
            	File.createFile(result, nomeArquivo,  debug);
            }
            
            return result;
    		
    	} catch (Exception ex) {
    		throw new Exception(ex.getMessage());
    	}
    }
    
	private void executarExcecao(Response response, JSONObject jsonObject, Exception exception, String nomeArquivo) throws Exception {
			
		if(response != null) {
			
			File.createException(response.code() + " - " + response.message(), nomeArquivo, debug);
			throw new Exception(response.code() + " - " + response.message());
	
		} else if (jsonObject != null ) {
			
			File.createException(jsonObject.getString("status") + " " + jsonObject.getString("detail"), nomeArquivo, debug);
			
			throw new Exception(jsonObject.getString("status") + " " + jsonObject.getString("detail"));
			
			
		} else if (exception != null) {
			File.createException("Erro 500 " + exception.getMessage(), nomeArquivo, debug);
			throw new Exception(exception.getMessage());
		}
		
	}
    
    
//    public static void main(String[] args) {
//        try {
//            CapturarEventos captura = new CapturarEventos(true);
//            //captura.salvarEventos("8e4b9bb90a7b2f891ebea5c2d98d67feab4f4a7d", "180809-53,0", true);
//            captura.salvarEventos("5471fbfb0b40908fad07f9ea3013df5ee0807adb", "180809-68,0", true, "Teste");
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    
}