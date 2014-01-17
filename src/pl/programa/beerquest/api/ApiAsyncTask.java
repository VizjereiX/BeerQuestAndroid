package pl.programa.beerquest.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.NullPointerException;

import pl.programa.beerquest.app.App;
import android.os.AsyncTask;

/**
 * class for execution asynchronous requests
 * @author Programa.pl
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, JSONObject> {

	private final static String ERROR_PARSE = "Blad parsowania odpowiedzi";
	private final static String ERROR_INCOMPLETE = "B³êdne dane logowania";

	ApiCallback callback;
	Boolean isSuccess;
	HttpRequestBase request;

	int httpStatus = 0;

	public ApiAsyncTask(HttpRequestBase request, ApiCallback callback) {
		this.callback = callback;
		this.request = request;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		App.apiCount++;
	}

	protected void onPostExecute(JSONObject response) {
		App.apiCount--;
		App.logv("onPostExecute");
		App.logv(response.toString());
		Object data = null;
		try {
			Integer status = response.getInt(Api.STATUS);
			if (status == 0) {
				try {
					data = response.getJSONArray(Api.DATA);
					App.logv(data.toString());
				} catch (Exception e) {
					data = response.getJSONObject(Api.DATA);
				}
				callback.onResponse(data, status, "", httpStatus);
			} else {
				// ERROR, get message
				String message = response.getString(Api.MESSAGE);
				callback.onResponse(null, status, message, httpStatus);
			}
		} catch (JSONException e) {
			App.logv("JSON COUNTER RESPONSE: " + response);
			e.printStackTrace();
			App.logv(e.toString());
			callback.onResponse(null, -1, ERROR_PARSE, httpStatus);
		} catch (NullPointerException e) {
			e.printStackTrace();
			App.logv(data + " Null pointer exception: " + e.toString());
			callback.onResponse(null, 1, ERROR_INCOMPLETE, httpStatus);
		} catch (Exception e) {
			e.printStackTrace();
			App.logv(data + "Other exception: " + e.toString());
			callback.onResponse(null, -1, ERROR_INCOMPLETE, httpStatus);
		}
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		isSuccess = false;
		String response = getResponse();

		App.logv("-------------------API RESPONSE " + request.getMethod() + " STRING: " + request.getURI());
		App.logv("aaa: [[[+++" + response + "+++]]]");
		App.logv("-------------------API RESPONSE STRING END!");

		JSONObject responseJson;
		try {
			responseJson = new JSONObject(response.toString());

			isSuccess = true;
			return responseJson;
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject errorObj = new JSONObject();
		try {
			errorObj.put("status", -1l);
			errorObj.put("message", ERROR_INCOMPLETE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorObj;
	}

	public String getResponse() {
		String jsonResponse = null;
		HttpClient loginHttpClient = CustomHttpClient.getHttpClient();
		try {
			HttpResponse response = loginHttpClient.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			httpStatus = response.getStatusLine().getStatusCode();
			App.logv("HTTP STATUS: " + httpStatus);
			jsonResponse = getResponseString(in);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResponse;
	}

	private String getResponseString(BufferedReader in) throws IOException {
		StringBuffer sb = new StringBuffer("");
		String line = "";
		String NL = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			sb.append(line + NL);
		}
		in.close();
		return sb.toString();
	}

}