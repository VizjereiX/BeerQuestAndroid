package pl.programa.beerquest.api;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Login;
import pl.programa.beerquest.utils.JsonHelper;
import android.content.Context;

/**
 * class for communication with server side Api
 * @author Programa.pl
 */
public class Api {

	//local API
	public static final String API = "http://beerquest.a.ext.programa.pl/api/";
	
	public static final String HEADER = "http-x-bq-token";
	public static final String STATUS = "status";
	public static final String DATA = "data";
	public static final String MESSAGE = "message";

	//controllers names
	public static final String CONTROLLER_LOGIN = "user/login";
	public static final String CONTROLLER_TEST = "test";
	
	public static final String token = "#beerquest#"; 

	public static final String UTF8 = "UTF-8";
	public static final String ACCEPT = "Accept";
	public static final String APPLICATION_JSON = "application/json";
	
	public static void sendSomething (String jasonString, Context appContext, ApiCallback callback){
		Api.sendPostJson(jasonString, API + CONTROLLER_TEST, appContext, callback);            
	}
	

	public static void login(String token, Context appContext, ApiCallback callback) {
		String loginJson = "{email:\"email\",google_id:\"dsae321321\"}";
		String url = API + CONTROLLER_LOGIN;
		HttpRequestBase request = preparePostRequest(appContext, url, loginJson);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	public static void sendPostJson(String jsonString, String url, Context appContext, ApiCallback callback) {
		HttpRequestBase request = preparePostRequest(appContext, url, jsonString);
		App.logv("sending json:");
		App.logv(jsonString);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	private static HttpPost preparePostRequest(Context appContext, String url, String jsonString) {
		App.logv("POST REQUEST");
		HttpPost request = new HttpPost(url);
		App.logv("request url: " + url);

		HttpParams params = new BasicHttpParams();
		request.setParams(params);

		try {
			String encoded = new String(jsonString.getBytes(UTF8));
			App.logv("json encoded: " + encoded);
			request.setEntity(new StringEntity(encoded, UTF8));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		String token = getApp(appContext).getToken();
		App.logv("token: " + token);

		Header header = new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		request.addHeader(header);
		header = new BasicHeader(Api.HEADER, token);
		request.addHeader(header);
		header = new BasicHeader(ACCEPT, APPLICATION_JSON);
		request.addHeader(header);

		return request;
	}

	private static App getApp(Context appContext) {
		return (App) appContext;
	}
}
