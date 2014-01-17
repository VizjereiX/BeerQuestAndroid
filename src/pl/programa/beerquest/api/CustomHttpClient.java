package pl.programa.beerquest.api;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import pl.programa.beerquest.app.App;

/**
 * class for preparation of Http requests
 * @author Programa.pl
 */
public class CustomHttpClient {
	
	public static final Integer STATUS_OK = 200;
	private static HttpClient customHttpClient;

	// private constructor prevents instantiation
	private CustomHttpClient() {
	}

	public static synchronized HttpClient getHttpClient() {
		if (customHttpClient == null) {
			try{
                  HttpParams params = new BasicHttpParams();
                  HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                  HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
                  HttpProtocolParams.setUseExpectContinue(params, true);
                  HttpProtocolParams.setUserAgent(params, "Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                  ConnManagerParams.setTimeout(params, 10000); // doubled
                  HttpConnectionParams.setConnectionTimeout(params, 10000);
                  HttpConnectionParams.setSoTimeout(params, 10000);
                  SchemeRegistry schReg = new SchemeRegistry();
                  schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                  schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
                  ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
                  customHttpClient = new DefaultHttpClient(conMgr, params);
			} catch(Exception e){
				App.logv("BUILDING HTTP CLIENT EXCEPTION");
			}
		}
		return customHttpClient;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
