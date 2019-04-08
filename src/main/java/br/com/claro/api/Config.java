package br.com.claro.api;

/**
 *
 * @author Elison Correa
 */

public class Config {

	private String apiuser;
	private String apipassword;
	private String filesPath;
	private static String filespathtxt;
	private static String filesPathReprocess;
	private static String EndPoint;
	private static String filesPathexception;

	public static String getFilesPathexception() {
		return filesPathexception;
	}

	public static void setFilesPathexception(String filesPathexception) {
		Config.filesPathexception = filesPathexception;
	}

	public static String getEndPoint() {
		return EndPoint;
	}

	public static void setEndPoint(String endPoint) {
		Config.EndPoint = endPoint;
	}

	public String getApiuser() {
		return apiuser;
	}

	public void setApiuser(String apiuser) {
		this.apiuser = apiuser;
	}

	public String getApipassword() {
		return apipassword;
	}

	public void setApipassword(String apipassword) {
		this.apipassword = apipassword;
	}

	public String getFilesPath() {
		return filesPath;
	}

	public void setFilesPath(String filesPath) {
		this.filesPath = filesPath;
	}

	public static String getFilespathtxt() {
		return filespathtxt;
	}

	public static void setFilespathtxt(String filespathtxt) {
		Config.filespathtxt = filespathtxt;
	}

	public static String getFilesPathReprocess() {
		return filesPathReprocess;
	}

	public static void setFilesPathReprocess(String filesPathReprocess) {
		Config.filesPathReprocess = filesPathReprocess;
	}

}