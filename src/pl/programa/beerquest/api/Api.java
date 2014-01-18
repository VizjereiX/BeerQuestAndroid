package pl.programa.beerquest.api;

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;
import pl.programa.beerquest.utils.JsonHelper;
import android.content.Context;

/**
 * class for communication with server side Api
 * 
 * @author Programa.pl
 */
public class Api {

	// local API
	public static final String API = "http://beerquest.a.ext.programa.pl/api/";

	public static final String HEADER = "http-x-bq-token";
	public static final String STATUS = "status";
	public static final String DATA = "data";
	public static final String MESSAGE = "message";

	// controllers names

	public static final String CONTROLLER_LOGIN = "user/login";
	public static final String CONTROLLER_RECOGNIZE = "quest:%%/monster";
	public static final String CONTROLLER_QUEST = "quest";	
	public static final String CONTROLLER_TEST = "test";

	public static final String token = "#beerquest#";

	public static final String UTF8 = "UTF-8";
	public static final String ACCEPT = "Accept";
	public static final String APPLICATION_JSON = "application/json";

	private static final String CONTROLLER_QUEST_NEW = "quest/new";

	private static final String CONTROLLER_REGISTER = "push/register";

	private static final String CONTROLLER_USER_LOCATION = "user/location";

	public static void sendSomething(String jasonString, Context appContext,
			ApiCallback callback) {
		Api.sendPostJson(jasonString, API + CONTROLLER_TEST, appContext,
				callback);
	}

	public static void getQuests(String name, Context appContext, ApiCallback callback) {
		String url = API + CONTROLLER_QUEST;
		HttpRequestBase request = preparePostRequest(appContext, url, "{}");
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	public static void login(String name, Context appContext, ApiCallback callback) {
		String token = App.getDeviceId(appContext);
		String loginJson = "{\"email\":\"" + name + "\", \"google_id\":\"" + token + "\"}";
		String url = API + CONTROLLER_LOGIN;
		HttpRequestBase request = preparePostRequest(appContext, url, loginJson);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	public static void recognize(String idJson, Context appContext,
			ApiCallback callback) {
		String url = API
				+ CONTROLLER_RECOGNIZE.replace("%%",
						Math.abs(new Random().nextInt()) + "");
		HttpRequestBase request = preparePostRequest(appContext, url, idJson);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	public static void sendPostJson(String jsonString, String url,
			Context appContext, ApiCallback callback) {
		HttpRequestBase request = preparePostRequest(appContext, url,
				jsonString);
		App.logv("sending json:");
		App.logv(jsonString);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	private static HttpPost preparePostRequest(Context appContext, String url,
			String jsonString) {
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

	public static void questNew(Quest quest, Context appContext,
			ApiCallback callback) {
		String url = API + CONTROLLER_QUEST_NEW;
		String json = JsonHelper.getGson().toJson(quest);
		HttpRequestBase request = preparePostRequest(appContext, url, json);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, callback);
		apiAsyncTask.execute();
	}

	public static void register(String regId, Context appContext) {
		String url = API + CONTROLLER_REGISTER;
		String json = "{\"push_type\":\"android\", \"push_id\":\"" + regId + "\"}";
		HttpRequestBase request = preparePostRequest(appContext, url, json);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request,
				new ApiCallback() {
					@Override
					public void onResponse(Object response, Integer status,
							String message, Integer httpStatus) {}
				});
		apiAsyncTask.execute();
	}

	public static void locationChange(double lat, double lng, Context appContext) {
		String url = API + CONTROLLER_USER_LOCATION;
		String json = "{\"lat\":" + lat + ", \"lng\":" + lng+ "}";
		
		HttpRequestBase request = preparePostRequest(appContext, url, json);
		ApiAsyncTask apiAsyncTask = new ApiAsyncTask(request, null);
		apiAsyncTask.execute();
	}
}
