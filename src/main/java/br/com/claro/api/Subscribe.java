package br.com.claro.api;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import br.com.claro.json.JSONArray;
import br.com.claro.json.JSONObject;
import br.com.claro.utils.Base64;
import br.com.claro.utils.ConfigUtils;
import br.com.claro.utils.File;

/**
*
* @author Elison Correa
*/
public class Subscribe {

    private boolean debug = false;
    private Config config;
    
    public Subscribe(boolean debug) {
        this.debug = debug;
        this.config = ConfigUtils.get();
    }
    
    /**
     * Método que cria uma nova inscrição
     * @param events
     * @param subscriptionTitle
     * @throws Exception 
     * 
     */
    public Subscription subscribe(JSONArray events, String subscriptionTitle) throws Exception{
        
        try {
        	JSONObject jo = new JSONObject();
            jo.put("subscriptionId", "8e4b9bb90a7b2f891ebea5c2d98d67feab4f4a7d");
            jo.put("events", events);
            jo.put("subscriptionTitle", subscriptionTitle);
          
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, jo.toString()); // corpo do body com os dados no formato JSON
            Request request = new Request.Builder()
             // .url(Credentials.END_POINT + "/events/subscriptions") // endpint
              .url(Config.getEndPoint()+ "/events/subscriptions")
              .post(body)
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Basic " + Base64.encodeBytes((config.getApiuser()+":"+config.getApipassword()).getBytes())) // Login da Aplicação
              .addHeader("Cache-Control", "no-cache")
              .build();

            Response response = client.newCall(request).execute();
            
            if (!response.isSuccessful()) { // teste pelo sucesso da requisição
               // throw new Exception(response.code() + " - " + response.message());
            	this.executarExcecao(response, null, null, subscriptionTitle);
            }
            
            String result = response.body().string();
            
            JSONObject retorno = new JSONObject(result);
            
            if (retorno.has("status")) { // sempre que é retornoado o atributo Status significa que algo deu errado
               // throw new Exception(retorno.getString("status") + " " + retorno.getString("detail"));
            	
            	this.executarExcecao(null, retorno, null, subscriptionTitle);
            }

            Subscription subscription = new Subscription();
            subscription.setNextPage(retorno.getString("nextPage"));
            subscription.setSubscriptionId(retorno.getString("subscriptionId"));
            subscription.setSubscriptionTitle(subscriptionTitle);
            
            if (debug) {
                System.out.println(result);
            }
            return subscription;
            
        } catch (Exception ex) {
        	throw new Exception(ex.getMessage());
        }
    }
    
    public String getNextPage(String subscriptionId, String subscriptionTitle, JSONArray events) throws Exception{
        
    	try {
    		JSONObject jo = new JSONObject();
            jo.put("subscriptionId", subscriptionId);
            jo.put("events", events);
            jo.put("subscriptionTitle", subscriptionTitle);
            
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, jo.toString()); // corpo do body com os dados no formato JSON
            Request request = new Request.Builder()
              //.url(Credentials.END_POINT + "/events/subscriptions") // endpint
             .url(Config.getEndPoint()+ "/events/subscriptions")
              .post(body)
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Basic " + Base64.encodeBytes((config.getApiuser()+":"+config.getApipassword()).getBytes())) // Login da Aplicação
              .addHeader("Cache-Control", "no-cache")
              .build();

            Response response = client.newCall(request).execute();
            
            if (!response.isSuccessful()) { // teste pelo sucesso da requisição
               // throw new Exception(response.code() + " - " + response.message());
            	
            	this.executarExcecao(response, null, null, subscriptionTitle);
            }
            
            String result = response.body().string();
            
            JSONObject retorno = new JSONObject(result);
            
            if (retorno.has("status")) { // sempre que é retornoado o atributo Status significa que algo deu errado
                //throw new Exception(retorno.getString("status") + " " + retorno.getString("detail"));
            	
            	this.executarExcecao(null, retorno, null, subscriptionTitle);
            	
            }

            if (debug) {
                System.out.println(result);
            }
            
            
            return retorno.getString("nextPage");
    		
    		
    		
    	} catch (Exception ex) {
    		throw new Exception(ex.getMessage());
    	}
            
    } 
    
    
	private void executarExcecao(Response response, JSONObject jsonObject, Exception exception, String subscriptionTitle) throws Exception {

		if(response != null) {
			
			File.createException(response.code() + " - " + response.message(), subscriptionTitle, debug);
			throw new Exception(response.code() + " - " + response.message());
	
		} else if (jsonObject != null ) {
			
			File.createException(jsonObject.getString("status") + " " + jsonObject.getString("detail"), subscriptionTitle, debug);
			
			throw new Exception(jsonObject.getString("status") + " " + jsonObject.getString("detail"));
		
		} else if (exception != null) {
			File.createException("Erro 500 " + exception.getMessage(), subscriptionTitle, debug);
			throw new Exception(exception.getMessage());
		}
		
	}
    
//    public static void main(String[] args) {
//        try {
//            Subscribe s = new Subscribe(true);
//
//            JSONArray ja = new JSONArray();
//            ja.put("chatMessageSent");
//
//            String nextPage = s.getNextPage("8e4b9bb90a7b2f891ebea5c2d98d67feab4f4a7d", "log", ja);
//            System.out.println(nextPage);
//            /*
//            System.out.println("ID: " + subs.getSubscriptionId());
//            System.out.println("Next Page: " + subs.getNextPage());
//            System.out.println("Links: ");
//            if (subs.getLinks() != null) {
//                for (String[] link : subs.getLinks()) {
//                    System.out.println("Rel: " + link[0] + "      href: " + link[1]);
//                }
//            }
//
//            System.out.println();
//            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
//*/
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
