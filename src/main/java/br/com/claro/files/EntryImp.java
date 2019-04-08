package br.com.claro.files;

public class EntryImp {

	//public class EntryImp {
//	
//	public static List<String> keysReverse;
//	public static List<String> keys;
//		
//	public static List<String> getKeysReverse(){
//		if(keysReverse== null) {
//			keysReverse = Arrays.asList(ColunasJson.PAINEL_COTAS_01.clone());
//			Collections.reverse(keysReverse);
//		}
//		return keysReverse;
//	}
//	public static List<String> getKeys(){
//		if(keys== null) {
//			keys = Arrays.asList(ColunasJson.PAINEL_COTAS_01.clone());
//		}
//		return keys;
//	}
//	
//
//
//	public static String keyPath(String s) {
////		String key = s.replaceAll("\\[(.*?)\\]", "");
//		String key = s.replaceAll("([0-9\\[\\]])", "");
//		
//		return key;//.substring(0, key.lastIndexOf(".")-1);		
//	}
//	
//	public static boolean existe(String keyPath) {
//		return getKeysReverse().contains(keyPath(keyPath));
//	}
//	
//	public static List<Linha> getanteriores(KeyPrefixObj atual,KeyPrefixObj anterior,List<String> listaIndice) {
//		List<Linha> l = new ArrayList<>();
//	
//		ListIterator<String> it = getKeysReverse().listIterator();
//		while(it.hasNext()) {
//			String kOf = it.next();
//			System.out.println(kOf+" == "+atual.getPath());
//			
//			if(kOf.equals(atual.getPath())) {
//				l.addAll(getKeyate(atual,anterior, it,listaIndice));
//				break;
////				list.addAll(getAnteriores(key, it));
//			}//mao bote break
//		}
//		return l;
//	}
//	
//	public static List<Linha> getKeyate(KeyPrefixObj atual,KeyPrefixObj anterior,ListIterator<String> it,List<String> listaIndice){
//		List<Linha> l = new ArrayList<Linha>();
//		while(it.hasNext()) {
//			String kOf = it.next();
//			System.out.println( kOf);
//			if(anterior != null && !kOf.equals(anterior.getPath())) {
//				l.add(new Linha(kOf, ""));
//			}else {
//				break;
//			}
//		}
//		Collections.reverse(l);
//		return l;
//	}
//	
//}
//


}
