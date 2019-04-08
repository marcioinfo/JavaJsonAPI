package br.com.claro.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import br.com.claro.api.Config;

public class ConfigUtils {

	public static Config get() {
		Config config = new Config();
		try {
			FileReader arq = new FileReader("config.dat");
			BufferedReader buffer = new BufferedReader(arq);

			String command = buffer.readLine();
			while (command != null) {
				String[] dados = command.split("=");

				if ("apiuser".equals(dados[0])) {
					config.setApiuser(dados[1]);
				} else if ("apipassword".equals(dados[0])) {
					config.setApipassword(dados[1]);
				} else if ("filespath".equals(dados[0])) {
					config.setFilesPath(dados[1]);
				} else if ("filespathtxt".equals(dados[0])) {
					Config.setFilespathtxt(dados[1]);
				} else if ("filesPathReprocess".equals(dados[0])) {
					Config.setFilesPathReprocess(dados[1]);
				} else if ("filesPathexception".equals(dados[0])) {
					Config.setFilesPathexception(dados[1]);
				} else if ("endPoint".equals(dados[0])) {
					Config.setEndPoint(dados[1]);
				}

				command = buffer.readLine();
			}

			arq.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

}