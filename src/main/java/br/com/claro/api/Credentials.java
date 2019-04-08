package br.com.claro.api;

import br.com.claro.utils.Base64;


/**
 *
 * @author Elison Correa
 */
public class Credentials {
    
//    public static final String END_POINT = "https://api.etadirect.com/rest/ofscCore/v1";
    private  static final String AUTH = "ora_api@clarobrasil3.test:ora@123";
    
    
    public static String getAuth() {
        return Base64.encodeBytes(AUTH.getBytes());
    }
    
}
