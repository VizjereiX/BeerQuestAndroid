package pl.programa.beerquest.utils;

import com.google.gson.Gson;

/**
 * class to get JSON 
 * @author Programa.pl
 */
public class JsonHelper {

	private static Gson gson = new Gson();

	private JsonHelper() {
	}

	public static Gson getGson() {
		return gson;
	}
}
