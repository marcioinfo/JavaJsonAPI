package br.com.claro.oracle.api;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.claro.api.Config;
import br.com.claro.api.Subscribe;
import br.com.claro.api.Subscriptions;
import br.com.claro.controllers.Main;
import br.com.claro.external.CapturarEventos;
import br.com.claro.files.ColunasJson;
import br.com.claro.files.Inicio;
import br.com.claro.files.JSONFlattener;
import br.com.claro.files.Linha;
import br.com.claro.json.JSONArray;
import br.com.claro.utils.ConfigUtils;

/**
 *
 * @author Elison Correa
 */
public class OracleApi {

	private static boolean debug = false;

	public static void main(String[] args) {
		try {
			 //String[] args = {"q+","FORA_TOA_02","activityUpdated"};
			// String[] args = {"q+","FORA_TOA_02"};
			// String[] args = {"s+","FORA_TOA_01","activityCreated"};
			// String[] args = {"q+","EVENTOS_DE_MENSAGENS_03","broadcastStatusUpdate"};
			// String[] args = {"q+","EVENTOS_DE_MENSAGENS_01","chatMessageSent"};
			// String[] args = {"s+","RE_ROUTER","activityMoved"};
			// String[] args = {"d+","e75a91f036e03264e5c0dc996f92e5be2ea127a8","180905-775,0","FORA_TOA_02"};
			//String[] args = {"l+"};

			if (args == null || args.length < 1) {
				throw new Exception(
						"Lista de parâmetros de inicialização vazia.");
			}

			String command = args[0].toLowerCase();

			if (command.equals("c") || command.equals("c+")) {

				debug = command.equals("c+");

				if (args.length < 3) {
					throw new Exception(
							"O camando c exige dois parâmetros adicionais: Título e Eventos.");
				}
				String titulo = args[1];
				String eventos = args[2];
				JSONArray events = new JSONArray();
				String[] evts = eventos.split(",");
				for (int i = 0; i < evts.length; i++) {
					events.put(evts[i]);
				}
				Subscribe subscribe = new Subscribe(debug);
				subscribe.subscribe(events, titulo);

			} else if (command.equals("l") || command.equals("l+")) {
				debug = command.equals("l+");
				Subscriptions subscriptions = new Subscriptions(debug);
				subscriptions.listSubscriptions();
			} else if (command.equals("d") || command.equals("d+")) {
				debug = command.equals("d+");
				if (args.length < 4) {
					throw new Exception(
							"O camando d exige três parâmetros adicionais: Id da inscrição, Página e nome do arquivo.");
				}

				String id = args[1];
				String pagina = args[2];

				String arquivo = args[3];
				if (arquivo == null || arquivo.isEmpty()) {
					throw new Exception("Nome do arquivo inválido");
				}

				CapturarEventos capturar = new CapturarEventos(debug);
				capturar.salvarEventos(id, pagina, true, arquivo);

				if (debug) {
					System.out.println("Finalizado com sucesso!");
				}
			} else if (command.equals("s") || command.equals("s+")) {
				debug = command.equals("s+");

				if (args.length < 3) {
					throw new Exception(
							"O camando s exige que seja informado o nome do arquivo e o evento assistido");
				}

				String arquivo = args[1];
				if (arquivo == null || arquivo.isEmpty()) {
					throw new Exception("Nome do arquivo inválido");
				}

				String evento = args[2];
				if (evento == null || evento.isEmpty()) {
					throw new Exception("Evento inválido");
				}

				Main main = new Main(arquivo, evento, debug);
				main.start();
			} else if (command.equals("p") || command.equals("p+")) {

				debug = command.equals("p+");

				// /ConfigUtils proc = new ConfigUtils();

				if (args.length < 2) {
					throw new Exception(
							"É necessário fornecer ao menos o nome de 1 arquivo");
				}

				for (String arg : args) {
					String[] atributoEscolhido = Inicio
							.verificarColuna(args[1]);

					if (atributoEscolhido == null) {
						throw new Exception("Erro");
					}

					for (int i = 1; i < args.length; i++) {
						for (Field field : ColunasJson.class
								.getDeclaredFields()) {
							if (field.getName().equals(arg)) {

								ConfigUtils.get();
								List<Linha> l = JSONFlattener.parseJson(
										Config.getFilesPathReprocess() + arg
												+ ".json", atributoEscolhido);

								if (debug) {
									System.out
											.println(JSONFlattener
													.dataBaseFormat(
															l,
															new File(
																	Config.getFilesPathReprocess()
																			+ arg
																			+ ".txt")));
								}
							}
						}
					}

				}

			} else if (command.equals("q") || command.equals("q+")) {

				debug = command.equals("q+");

				if (args.length < 3) {
					throw new Exception(
							"O camando q exige que seja informado o nome do arquivo e o evento assistido");

				}

				String arquivo = args[1];
				if (arquivo == null || arquivo.isEmpty()) {
					throw new Exception("Nome do arquivo inválido");
				}

				String evento = args[2];
				if (evento == null || evento.isEmpty()) {
					throw new Exception("Evento inválido");
				}

				Main main = new Main(arquivo, evento, debug);
				main.start();

				String[] atributoEscolhido = Inicio.verificarColuna(arquivo);

				if (atributoEscolhido == null) {
					throw new Exception("Erro");
				}

				SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
				Calendar data = Calendar.getInstance();

				for (int i = 1; i < args.length; i++) {
					for (Field field : ColunasJson.class.getDeclaredFields()) {

						if (field.getName().equals(arquivo)) {

							List<Linha> l = JSONFlattener.parseJson(
									Config.getFilespathtxt() + "/" + arquivo
											+ ".json", atributoEscolhido);

							if (debug) {
								System.out
										.println(JSONFlattener.dataBaseFormat(
												l,
												new File(
														Config.getFilespathtxt()
																+ "/"
																+ "ff_"
																+ arquivo
																+ "_"
																+ formatoData
																		.format(data
																				.getTime())
																+ ".txt")));
							}
						}
					}
				}

			} else {
				throw new Exception("Comando inválido!");
			}
			
			System.out.println(0);

		} catch (Exception e) {
			System.err.println(1);
			System.err.println("Erro: " + e.getMessage());
		}

	}
}