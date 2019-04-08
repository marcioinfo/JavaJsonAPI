package br.com.claro.api;


import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
public class Subscriptions {
    
    private boolean debug;
    private Config config;
    
    public Subscriptions(boolean debug) {
        this.debug = debug;
        this.config = ConfigUtils.get();
    }
    
    /**
     * Método para retornar uma lista de inscrições
     * @return 
     * @throws Exception 
     */
    public List<Subscription> listSubscriptions() throws Exception {
        try{
        	List<Subscription> lista = new ArrayList<>();
        	
        	OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
             // .url(Credentials.END_POINT + "/events/subscriptions") // Endpint
              .url(Config.getEndPoint()+ "/events/subscriptions")
              .get()
              .addHeader("Authorization", "Basic " + Base64.encodeBytes((config.getApiuser()+":"+config.getApipassword()).getBytes())) // autorização/login
              .addHeader("Accept", "application/json")
              .addHeader("Cache-Control", "no-cache")
              .build();

            Response response = client.newCall(request).execute();
            
            if (!response.isSuccessful()) { // teste pelo sucesso da requisição
               // throw new Exception(response.code() + " - " + response.message());
            	String file = null;
            	this.executarExcecao(response, null, null, file);
            }
            
            String result = response.body().string();
            
            
            JSONObject retorno = new JSONObject(result);
            
            
            if (retorno.has("status")) { // sempre que é retornoado o atributo Status significa que algo deu errado
            //    throw new Exception(retorno.getString("status") + " " + retorno.getString("detail"));
            	String file = null;
            	this.executarExcecao(null, retorno, null, file);
            	
            }

            if (this.debug) {
                System.out.println(result);
            }
            
            if (retorno.has("totalResults") && retorno.getInt("totalResults") > 0) {
                
                JSONArray ja = retorno.getJSONArray("items");
                
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject subs = ja.getJSONObject(i);
                    Subscription subscription = new Subscription();

                    // Monto o objeto subscription utilizando os dados retornados
                    if (subs.has("subscriptionId") && !subs.isNull("subscriptionId")) {
                        subscription.setSubscriptionId(subs.getString("subscriptionId"));
                    }
                    if (subs.has("nextPage") && !subs.isNull("nextPage")) {
                        subscription.setNextPage(subs.getString("nextPage"));
                    }
                    if (subs.has("subscriptionTitle") && !subs.isNull("subscriptionTitle")) {
                        subscription.setSubscriptionTitle(subs.getString("subscriptionTitle"));
                    }

                    JSONArray eventos = subs.getJSONArray("events");
                    String[] evnt = new String[eventos.length()];
                    
                    for(int z = 0; z < eventos.length(); z++) {
                        evnt[z] = eventos.getString(z);
                    }
                    
                    subscription.setEvents(evnt);
                    
                    if (subs.has("links") && !subs.isNull("links")) {
                        JSONArray jas = subs.getJSONArray("links");

                        List<String[]> links = new ArrayList<>();
                        for (int j = 0; j < jas.length(); j++) {
                            JSONObject link = jas.getJSONObject(j);
                            links.add(new String[] {link.getString("rel"), link.getString("href")});
                        }

                        subscription.setLinks(links);
                    }
                    
                    
                    lista.add(subscription);
                }
            
            }
        
            return lista;
        	
        	
        	
        } catch (Exception e) {
        	
    		throw new Exception(e.getMessage());
        	
        }
        
    }
    
    
    private void executarExcecao(Response response, JSONObject jsonObject, Exception exception, String file) throws Exception {
		
		if(response != null) {
			
			File.createException(response.code() + " - " + response.message(), file, debug);
			throw new Exception(response.code() + " - " + response.message());
	
		} else if (jsonObject != null ) {
			
			File.createException(jsonObject.getString("status") + " " + jsonObject.getString("detail"), file, debug);
			
			throw new Exception(jsonObject.getString("status") + " " + jsonObject.getString("detail"));
		
		
		} else if (exception != null) {
			File.createException("Erro 500 " + exception.getMessage(), file, debug);
			throw new Exception(exception.getMessage());
		}
		
	}
    
    public static void main (String [] args) {
        try {
            
            
            Subscriptions s = new Subscriptions(true);
            List<Subscription> lista = s.listSubscriptions();
            for (Subscription subscription : lista) {
                System.out.println("ID: " + subscription.getSubscriptionId());
                System.out.println("Title: " + subscription.getSubscriptionTitle());
                System.out.println("Next Page: " + subscription.getNextPage());
                System.out.println("Links: ");
                if (subscription.getLinks() != null) {
                    for (String[] link : subscription.getLinks()) {
                        System.out.println("Rel: " + link[0] + "      href: " + link[1]);
                    }
                }
                
                System.out.println();
                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}