package br.com.claro.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import br.com.claro.json.JSONArray;
import br.com.claro.json.JSONObject;
import br.com.claro.utils.Base64;
import br.com.claro.utils.ConfigUtils;
//import br.com.claro.json.JSONArray;
//import br.com.claro.json.JSONObject;

/**
 * Classe respos�vel pele tratamento da coleta de arquivos di�rios de uma subcri��o
 * @author Elison Correa
 */
public class SubscriptionsData {
    private boolean debug = false;
    private Config config;
    
    public SubscriptionsData (boolean debug) {
        this.debug = debug;
        this.config = ConfigUtils.get();
    }
    
    /**
     * Retorna a lista de eventos de uma inscrição
     * @param subscription
     * @return
     * @throws Exception 
     */
    public List<Event> getEvents(Subscription subscription) throws Exception{
        
        try {
        	List<Event> events = new ArrayList<>();
        	
            OkHttpClient client = new OkHttpClient();

            String page = "";
            if (subscription.getNextPage() != null) {
                page = "&page="+subscription.getNextPage();
            }
            
            Request request = new Request.Builder()
              //.url(Credentials.END_POINT + "/events/?subscriptionId="+subscription.getSubscriptionId()+page) // Endpoint passando o subcription id e o next page
              
              .url(Config.getEndPoint()+ "/events/?subscriptionId="+subscription.getSubscriptionId()+page)
              .get()
              .addHeader("Authorization", "Basic " + Base64.encodeBytes((config.getApiuser()+":"+config.getApipassword()).getBytes()))
              .addHeader("Accept", "application/json")
              .addHeader("Cache-Control", "no-cache")
              .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) { // teste pelo sucesso da requisição
                throw new Exception(response.code() + " - " + response.message());
            }
            
            String result = response.body().string();
            
            JSONObject retorno = new JSONObject(result);
            
            if (retorno.has("status")) { // sempre que é retornoado o atributo Status significa que algo deu errado
                throw new Exception(retorno.getString("status") + " " + retorno.getString("detail"));
            }

            if (retorno.has("found") && retorno.getBoolean("found")) {
                
                // atualizo o next page da subscription
                if (retorno.has("nextPage") && !retorno.isNull("nextPage")) {
                    subscription.setNextPage(retorno.getString("nextPage"));
                }
                
                JSONArray ja = retorno.getJSONArray("items");
                for (int i = 0; i < ja.length(); i++) {
                    
                    JSONObject item = ja.getJSONObject(i);
                    
                    Event event = new Event();
                    event.setSubscriptionId(subscription.getSubscriptionId());
                    event.setEventType(item.getString("eventType"));
                    event.setTime(item.getString("time"));
                    event.setUser(item.getString("user"));
                    
                    JSONObject details = item.getJSONObject("activityDetails");
                    ActivityDetail activityDetail = new ActivityDetail();
                    activityDetail.setActivityId(details.getString("activityId"));
                    activityDetail.setResourceId(details.getString("resourceId"));
                    activityDetail.setData(details.getString("date"));
                    
                    event.setActivityDetails(activityDetail);
                    
                    JSONObject inventoryDetails = item.getJSONObject("requiredInventoryDetails");
                    RequiredInventoryDetail requiredInventoryDetail = new RequiredInventoryDetail();
                    requiredInventoryDetail.setInventoryType(inventoryDetails.getString("inventoryType"));
                    
                    event.setRequiredInventoryDetail(requiredInventoryDetail);
                    
                    
                    JSONObject inventoryChanges = item.getJSONObject("requiredInventoryChanges");
                    RequiredInventoryChange requiredInventoryChange = new RequiredInventoryChange();
                    requiredInventoryChange.setQuantity(inventoryChanges.getInt("quantity"));
                    
                    event.setRequiredInventoryChange(requiredInventoryChange);
                    
                    
                    events.add(event);
                }
            }
            
            return events;
            
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    
    
    /*
    public static void main(String[] args) {
        try {
            Event e = new Event();

            e.setEventType("requiredInventoryCreated");
            e.setSubscriptionId("8e4b9bb90a7b2f891ebea5c2d98d67feab4f4a7d");
            e.setTime("2016-04-25 12:36:11");
            e.setUser("myroot");

            ActivityDetail activityDetail = new ActivityDetail();
            activityDetail.setActivityId("19828");
            activityDetail.setData("2016-04-25");
            activityDetail.setResourceId("Ira1");
            e.setActivityDetails(activityDetail);

            RequiredInventoryChange requiredInventoryChange = new RequiredInventoryChange();
            requiredInventoryChange.setQuantity(1);
            e.setRequiredInventoryChange(requiredInventoryChange);

            RequiredInventoryDetail requiredInventoryDetail = new RequiredInventoryDetail();
            requiredInventoryDetail.setInventoryType("CABLE_MODEM");
            e.setRequiredInventoryDetail(requiredInventoryDetail);

            File.createFile(e, e.getActivityDetails().getActivityId() + "-"+e.getRequiredInventoryDetail().getInventoryType(), "arquivos");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    
    public List<Event> getTestEvent(Subscription subscription) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Event e = new Event();

            Random r = new Random();
            
            e.setEventType("requiredInventoryCreated");
            e.setSubscriptionId(subscription.getSubscriptionId());
            e.setTime("2016-04-25 12:36:11");
            e.setUser("myroot");

            ActivityDetail activityDetail = new ActivityDetail();
            activityDetail.setActivityId(String.valueOf(r.nextInt(99999)));
            activityDetail.setData("2016-04-25");
            activityDetail.setResourceId("Ira1");
            e.setActivityDetails(activityDetail);

            RequiredInventoryChange requiredInventoryChange = new RequiredInventoryChange();
            requiredInventoryChange.setQuantity(1);
            e.setRequiredInventoryChange(requiredInventoryChange);

            RequiredInventoryDetail requiredInventoryDetail = new RequiredInventoryDetail();
            requiredInventoryDetail.setInventoryType("CABLE_MODEM");
            e.setRequiredInventoryDetail(requiredInventoryDetail);
            
            
            events.add(e);
        }
        
        return events;
    }
    
}