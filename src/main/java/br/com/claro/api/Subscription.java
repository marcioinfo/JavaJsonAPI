package br.com.claro.api;

import java.util.List;


/**
 * Classe que armazena os dados de uma inscri��o
 * @author Elison Correa
 */
public class Subscription {
    
    private String nextPage;
    private String subscriptionId;
    private String subscriptionTitle;
    private List<String[]> links;
    private String[] events;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public List<String[]> getLinks() {
        return links;
    }

    public void setLinks(List<String[]> links) {
        this.links = links;
    }

    public String getSubscriptionTitle() {
        return subscriptionTitle;
    }

    public void setSubscriptionTitle(String subscriptionTitle) {
        this.subscriptionTitle = subscriptionTitle;
    }

    public String[] getEvents() {
        return events;
    }

    public void setEvents(String[] events) {
        this.events = events;
    }
    
    
    
}