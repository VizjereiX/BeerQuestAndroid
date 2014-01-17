package pl.programa.beerquest.utils;

import pl.programa.beerquest.app.App;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * class internet helper
 * @author Programa.pl
 */
public class InternetHelper {

	public final static String ACCEPT = "Accept";
	public final static String APPLICATION_JSON = "application/json";

	// Check if we have a valid Internet Connection on the device.
	public static boolean getInternetInfo(Context ctx) {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			App.logi("NOT_CONNECTED");
			return false;
		} else {
			info.getState();
			if (info != null && info.getState().equals(State.CONNECTING)) {
				App.logi("CONNECTING");
				return false;
			} else {
				App.logi("CONNECTED");
				return true;
			}
		}
	}
}
