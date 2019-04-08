package br.com.claro.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class JSONFlattener {
	private static final List<String> listaArrays = new ArrayList<String>();

	private static KeyPrefixObj anterior = null;

	private static final Logger LOGGER = Logger.getLogger(JSONFlattener.class.getName());

	private static List<String> keysReverse;
	private static List<String> keys;

	private static String keyPath(String s) {
		String key = s.replaceAll("\\[(.*?)\\]", "");
		
		// String key = s.replaceAll("\\d{6}");
		// String key = s.replaceAll("(^[a-z0-9_-]{3,15}$)", "");

		return key;// .substring(0, key.lastIndexOf(".")-1);
	}

	private static boolean existe(String keyPath) {
		return keysReverse.contains(keyPath(keyPath));
	}

	private static List<Linha> getanteriores(KeyPrefixObj atual, KeyPrefixObj anterior, List<String> listaIndice) {
		List<Linha> l = new ArrayList<Linha>();

		ListIterator<String> it = keysReverse.listIterator();
		while (it.hasNext()) {
			String kOf = it.next();
			// System.out.println(kOf+" == "+atual.getPath());

			if (kOf.equals(atual.getPath())) {
				l.addAll(getKeyate(atual, anterior, it, listaIndice));
				break;
			}
		}
		return l;
	}

	private static List<Linha> getKeyate(KeyPrefixObj atual, KeyPrefixObj anterior, ListIterator<String> it,
			List<String> listaIndice) {
		List<Linha> l = new ArrayList<Linha>();
		while (it.hasNext()) {
			String kOf = it.next();
			// System.out.println( kOf);
			if (anterior != null && !kOf.equals(anterior.getPath())) {
				l.add(new Linha(kOf, ""));
			} else {
				break;
			}
		}
		Collections.reverse(l);
		return l;
	}

	public static List<Linha> parseJson(String file, String[] propriedades) throws FileNotFoundException {
		List<Linha> flatJson = null;

		try {

			keysReverse = Arrays.asList(propriedades.clone());
			Collections.reverse(keysReverse);
			keys = Arrays.asList(propriedades.clone());

			LOGGER.log(Level.INFO, "Lendo Arquivo Json: " + file);
			InputStream is = new FileInputStream(new File(file));

			JsonReader reader = Json.createReader(is);

			LOGGER.log(Level.INFO, "Criando objeto json: " + file);
			JsonObject JsonObject = reader.readObject();

			flatJson = parse(JsonObject);
		} catch (Exception je) {
			LOGGER.log(Level.SEVERE, je.getMessage());
			je.printStackTrace();
		}

		return flatJson;
	}

	private static List<Linha> parse(JsonObject JsonObject) {
		List<Linha> lista = new ArrayList<Linha>();
		LOGGER.log(Level.INFO, "Iniciando Parse: " + JsonObject);
		flatten(JsonObject, null, "", lista);
		lista.addAll(buscaElementosPosteriores(anterior, ""));
		LOGGER.log(Level.INFO, "Parse Termidado: ");
		LOGGER.log(Level.INFO, "Resultado: " + mostrar(lista));

		return lista;
	}

	private static void flatten(JsonObject obj, KeyPrefixObj anterior_, String prefix, List<Linha> lista) {
		Iterator<?> iterator = obj.entrySet().iterator();
		String _prefix = prefix != "" ? prefix + "." : "";

		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Entry<String, ?> en = (Entry<String, ?>) iterator.next();

			String key = en.getKey();
			// System.out.println("CHAVE: "+prefix +" "+ key+"<-");

			if (en.getValue() instanceof JsonObject) {

				JsonObject JsonObject = (JsonObject) obj.get(key);
				flatten(JsonObject, anterior, _prefix + key + "[1]", lista);

			} else if (en.getValue() instanceof JsonArray) {

				JsonArray jsonArray = (JsonArray) en.getValue();

				if (jsonArray.size() < 1) {
					continue;
				}

				flatten(jsonArray, anterior, _prefix + key, lista);

			} else {

				String value = en.getValue().toString();

				if (existe(_prefix + key)) {
					KeyPrefixObj atual = new KeyPrefixObj(key, _prefix, obj);
					if (isNovoPath(_prefix, anterior)) {

						// lista.addAll(buscaElementosPosteriores(anterior, prefix));
						// mostrar(lista);

						// lista.addAll(buscaValoresAnterios(atual, lista, prefix));
						Linha l = lista.get(lista.size() - 1);
						anterior = new KeyPrefixObj(l.key, "", obj);

						// mostrar(lista);
					}
					// System.out.println("CHAVE: "+_prefix + key+"<-");
					listaArrays.add(0, _prefix + key);
					// lista.addAll(getanteriores(atual, anterior, listaArrays));

					lista.add(new Linha(_prefix + key, value.replaceAll("\"", "")));
					anterior = new KeyPrefixObj(key, _prefix, obj);
				}
			}
		}

	}

	private static void flatten(JsonArray obj, KeyPrefixObj anterior_, String prefix, List<Linha> lista) {
		int length = obj.size();

		for (int i = 0; i < length; i++) {
			// System.out.println(obj.get(i));
			if (obj.get(i) instanceof JsonArray) {
				JsonArray jsonArray = (JsonArray) obj.get(i);

				if (jsonArray.size() < 1) {
					continue;
				}
				listaArrays.add(0, prefix + "[" + i + "]");
				flatten(jsonArray, anterior, prefix + "[" + i + "]", lista);
			} else if (obj.get(i) instanceof JsonObject) {
				JsonObject JsonObject = (JsonObject) obj.get(i);
				listaArrays.add(0, prefix + "[" + (i + 1) + "]");
				flatten(JsonObject, anterior, prefix + "[" + (i + 1) + "]", lista);
			} else {
				String value = obj.get(i).toString();
				KeyPrefixObj atual = new KeyPrefixObj("", prefix, obj);

				if (existe(prefix)) {
					if (isNovoPath(prefix, anterior)) {
						lista.addAll(buscaElementosPosteriores(anterior, prefix));
						// mostrar(lista);

						lista.addAll(buscaValoresAnterios(atual, lista, prefix));

						Linha l = lista.get(lista.size() - 1);
						anterior = new KeyPrefixObj(l.key, "", obj);

						// mostrar(lista);
					}

					lista.addAll(getanteriores(atual, anterior, listaArrays));
					listaArrays.add(0, prefix + "[" + (i + 1) + "]");
					lista.add(new Linha(prefix + "[" + (i + 1) + "]", value.replace("\"", "")));
					anterior = new KeyPrefixObj("", prefix + "[" + (i + 1) + "]", obj);
				}
				// mostrar(lista);
			}
		}
	}

	private static boolean isNovoPath(String prefixAtual, KeyPrefixObj anterio) {
		// String[] arrayPrefix = prefix.split("\\.");
		// String[] arrayAnterior = anterio.getPrefix().substring(0,
		// arrayPrefix.length-1).split("\\.");

		try {
			String preAtual = prefixAtual.replaceAll("[a-z.\\[\\]]", "");
			// String preAtual = prefixAtual.replaceAll("([a-z 0-9_-])", "");
			// String preAnterior = anterio.toString().replaceAll("([a-z 0-9_-])", "");
			String preAnterior = anterio.toString().replaceAll("[a-z.\\[\\]]", "");
			int atualL = preAtual.length();
			int anteriorL = preAnterior.length();

			if (anteriorL <= atualL) {
				atualL = Integer.parseInt(preAtual.substring(0, anteriorL));
				anteriorL = Integer.parseInt(preAnterior);
			} else {
				anteriorL = Integer.parseInt(preAnterior.substring(0, preAtual.length()));
				atualL = Integer.parseInt(preAtual);

			}

			// System.out.println(anterio.getPrefix() + " = "+prefixAtual);
			if (atualL > anteriorL) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage() + " JSONFlattener.isNovoPath : Não é um novo path.");
		}

		return false;
	}

	private static List<Linha> buscaValoresAnterios(KeyPrefixObj obj, List<Linha> lista, String prefix) {
		// System.out.println("entrou");

		List<Linha> l = new ArrayList<Linha>();
		Iterator<String> it = keysReverse.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (obj.getPath().equals(key) && it.hasNext()) {
				key = it.next();
				// Object[] en= flatJson.entrySet().toArray();
				for (int i = lista.size() - 1; -1 != i; i--) {
					Linha entry = lista.get(i);
					// System.out.println(entry.getKey()+" -- "+key );
					if (keyPath(entry.getKey()).equals(key)) {
						// System.out.println("-add-- ");
						l.add(entry);
						// map.put(entry.getKey()+contador++, entry.getValue());
						if (it.hasNext()) {
							key = it.next();
						} else {
							break;
						}
					}
				}
			}

		}
		Collections.reverse(l);

		return l;
	}

	private static List<Linha> buscaElementosPosteriores(KeyPrefixObj anterior_, String prefix) {
		List<Linha> l = new ArrayList<Linha>();
		Iterator<String> it = keys.iterator();

		if (anterior_ == null) {
			return l;
		}
		while (it.hasNext()) {
			String key = it.next();
			if (anterior_.getPath().equals(key)) {
				// key= it.next();
				while (it.hasNext()) {
					key = it.next();
					l.add(new Linha(key, ""));
				}
			}

		}
		return l;
	}

	public static String mostrar(List<Linha> l) {
		String primeira = keys.get(0);
		StringBuffer s = new StringBuffer();
		l.forEach((linha) -> {
			if (primeira.equals(linha.getKey().replaceAll("\\[(.*?)\\]", ""))) {
				// if(primeira.equals(linha.getKey().replaceAll("([0-9\\[\\]])", ""))) {
				s.append("\n");
			}
			s.append(String.format("%-60s : %s\n", linha.getKey(), linha.getValue()));
		});
		return s.toString();
	}

	public static StringBuilder dataBaseFormat(List<Linha> lista, File arquivo) throws IOException {
		StringBuilder s = null;
		StringBuilder cabecalho = new StringBuilder();
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		Integer contagemCabecalho = 0;

		for (String string : keys) {
			hashMap.put(string, contagemCabecalho);
			contagemCabecalho++;

			cabecalho.append(string);
			cabecalho.append("|");
		}
		s = new StringBuilder(cabecalho.substring(0, cabecalho.length() - 1) + "\n");

		// ajuste de lugares
		Iterator<Linha> itAjusteDeLugares = lista.iterator();
		HashMap<Integer, TreeMap<Integer, String>> hashFinal = new HashMap<Integer, TreeMap<Integer, String>>();
		Integer niveis = 0;
		while (itAjusteDeLugares.hasNext()) {
			Linha nextAjusteDeLugares = itAjusteDeLugares.next();
			// String chaveSemArray =
			// nextAjusteDeLugares.getKey().replaceAll("([0-9\\[\\]])","");
			String chaveSemArray = nextAjusteDeLugares.getKey().replaceAll("\\[(.*?)\\]", "");
			Integer lugarPosicaoDoHash = hashMap.get(chaveSemArray);

			if (lugarPosicaoDoHash != null
					&& (hashFinal.isEmpty() || (!hashFinal.isEmpty() && hashFinal.get(lugarPosicaoDoHash) == null))) {
				TreeMap<Integer, String> treeMapValores = new TreeMap<Integer, String>();
				treeMapValores.put(0, nextAjusteDeLugares.getValue());
				hashFinal.put(lugarPosicaoDoHash, treeMapValores);

			} else if (lugarPosicaoDoHash != null && !hashFinal.isEmpty()
					&& hashFinal.get(lugarPosicaoDoHash) != null) {
				TreeMap<Integer, String> treeMapValores = hashFinal.get(lugarPosicaoDoHash);
				Integer ultimaChave = treeMapValores.lastKey();
				Integer novoNivel = ultimaChave + 1;
				
				treeMapValores.put(novoNivel, nextAjusteDeLugares.getValue());
				hashFinal.put(lugarPosicaoDoHash, treeMapValores);
			}

		}

		// ajuste de chave que n�o tem na lista
		for (Integer i = 0; i < keys.size(); i++) {
			TreeMap<Integer, String> treeMapVerificacaoVazio = hashFinal.get(i);

			if (treeMapVerificacaoVazio == null || treeMapVerificacaoVazio.isEmpty()) {
				TreeMap<Integer, String> treMapVazio = new TreeMap<Integer, String>();

				for (Integer j = 0; j <= niveis; j++) {
					treMapVazio.put(j, "");
				}

				hashFinal.put(i, treMapVazio);
			}
		}

		/* ajuste feito para os as linhas que não apareciam */
		
		Integer nivelFinal = 0;
		while(hashFinal.get(nivelFinal) == null) {
			nivelFinal++;
		}
		
		int tamanhoNivelFinal = hashFinal.get(nivelFinal).size();
		
		for (Integer i = 0; i < tamanhoNivelFinal; i++) {

			for (Integer j = 0; j < keys.size(); j++) {
				TreeMap<Integer, String> valores = hashFinal.get(j);

				if (valores.get(i) != null && !valores.get(i).isEmpty()) {
					s.append(valores.get(i));
				} else {
					s.append("");
				}

				if (j == keys.size() - 1) {
					s.append("\n");
				} else {
					s.append("|");
				}
			}
		}

        OutputStreamWriter grava = new OutputStreamWriter(new FileOutputStream(arquivo),"UTF-8");
		//FileWriter grava = new FileWriter((arquivo));
		PrintWriter escreve = new PrintWriter(grava);
		escreve.println(s);
		escreve.close();
		grava.close();

		return s;
	}
}