package br.com.claro.controllers;

import java.util.List;

import br.com.claro.api.BancoDados;
import br.com.claro.api.Subscribe;
import br.com.claro.api.Subscription;
import br.com.claro.api.Subscriptions;
import br.com.claro.external.CapturarEventos;
import br.com.claro.json.JSONArray;
import br.com.claro.utils.File;


/**
 *
 * @author Elison Correa
 */
//public class Main extends Thread {
public class Main {
    
    private boolean executar = true;
    private boolean executando = true;
    private String nomeArquivo = "";
    private String evento = "";
    private boolean debug = false;
    
    public boolean isExecuting() {
        return this.executando;
    }
    
    public Main(String nomeArquivo, String evento, boolean debug) {
        this.nomeArquivo = nomeArquivo;
        this.evento = evento;
        this.debug = debug;
    }
    
    public void start() throws Exception {
    	while(executar) {
            executando = true;
            if (debug) {
                System.out.println("Iniciou análise das inscrições");
            }
            Subscriptions subscriptions = new Subscriptions(debug);
            List<Subscription> listaInscricoes = subscriptions.listSubscriptions(); // verifica se existe uma lista de inscrições

            Subscribe subscribe = new Subscribe(debug);
            boolean criar = true;
            
            if ((listaInscricoes.size() > 0)) {
                for (Subscription s : listaInscricoes) {
                    if (debug) {
                        System.out.println("Titulo da inscrição é: " + s.getSubscriptionTitle());
                    }
                    if (s.getSubscriptionTitle() != null && s.getSubscriptionTitle().equals(nomeArquivo)) {
                        String[] eventos = s.getEvents();
                        if (eventos != null) {
                            for (int i = 0; i < eventos.length; i++) {
                                if (eventos[i].equals(evento)) {
                                    criar = false;
                                    i = eventos.length;
                                }
                            }
                        }
                    }
                }
            }
            
            
            if (criar) {
                JSONArray events = new JSONArray();
                events.put(evento);
                Subscription subscription = subscribe.subscribe(events, nomeArquivo);
                subscription.setSubscriptionTitle(nomeArquivo);
                listaInscricoes.add(subscription);
                
                BancoDados bancoDados = new BancoDados();
                bancoDados.addDados(nomeArquivo, subscription.getNextPage());
            }
            
            
            BancoDados bancoDados = new BancoDados();
            CapturarEventos capturar = new CapturarEventos(debug);
            for (Subscription subscription : listaInscricoes) {
                
                if (subscription.getSubscriptionTitle() != null && nomeArquivo != null && !nomeArquivo.isEmpty() && subscription.getSubscriptionTitle().equals(nomeArquivo)) {
                    String[] eventos = subscription.getEvents();
                    if (eventos != null) {
                        for (int z = 0; z < eventos.length; z++) {
                            if (eventos[z].equals(evento)) {
                                if (debug) {
                                    System.out.println("Coletando dados da inscrição " + subscription.getSubscriptionId());
                                }
                                
                                String page = subscription.getNextPage();
                                
                                if (page == null) {
                                    if (bancoDados.has(subscription.getSubscriptionTitle())) {
                                        page = bancoDados.getPagina(subscription.getSubscriptionTitle());
                                    }
                                    
                                    /*
                                    if (page == null) {
                                        JSONArray a = new JSONArray();
                                        a.put(evento);
                                        subscription.setNextPage(subscribe.getNextPage(subscription.getSubscriptionId(), nomeArquivo, a));
                                    }*/
                                }
                                if (page != null) {
                                    String conteudo = capturar.salvarEventos(subscription.getSubscriptionId(), page, false, nomeArquivo);
                                    File.createFile(conteudo, this.nomeArquivo,  debug);
                                } else {
                                    if (debug) {
                                        System.out.println("ERRO: Pagina nula para a inscricao: " + nomeArquivo);
                                        
                                    }
                                }
                                z = eventos.length;
                                
                            }
                        }
                    }
                }
            }
            
            executar = false;
            //Thread.sleep(sleepTime * 60 * 1000); // faço o processo dormir
            
        }
        
        if (debug) {
            System.out.println("Arquivo gerado com sucesso!");
            
        }
    }
    
}