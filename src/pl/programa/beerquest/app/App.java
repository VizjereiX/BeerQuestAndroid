package pl.programa.beerquest.app;

import java.util.ArrayList;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;

import pl.programa.beerquest.LoginActivity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * class specifies application functional blocks
 * @author Programa.pl
 */
public class App extends Application {
	
	ArrayList <String> errors;
	//map refresh interval
	public static final int MILISEC_MAP_REFRESH = 15000;
	
    private static PlusClient mPlusClient;

	/**
	 * instance of App
	 */
	private static App instance;

	public App() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}
	
	public static PlusClient setMPlusClient(PlusClient mPlusC){
        mPlusClient = mPlusC;
		return mPlusClient;
	}

	public static PlusClient getMPlusClient(){
		return mPlusClient;
	}
	
	/**
	 * SPINNER API ASYNC TASK COUTNER inceremented on pre execute, decremented
	 * on post execute. Informs if any data is being downloaded or uploaded.
	 */
	public static int apiCount = 0;

	
	/**
	 * 
	 * @param ctx
	 * @param newLoginIntent
	 */
	public static void logoutClicked(Context ctx, Boolean newLoginIntent) {
		((App) ctx.getApplicationContext()).setLogin(null);
		logoutAndWipeData(ctx, newLoginIntent);
	}

	/**
	 * 
	 * @param ctx
	 * @param newLoginIntent
	 */
	public static void logout(Context ctx, Boolean newLoginIntent) {
		((App) ctx.getApplicationContext()).setLoggedIn(false);
		if (newLoginIntent) {
			Intent intent = new Intent(ctx, LoginActivity.class);
			ctx.startActivity(intent);
		}
	}

	/**
	 * 
	 * @param ctx
	 */
	public static void logoutClose(Context ctx) {
		((App) ctx.getApplicationContext()).setLoggedIn(false);
	}

	/**
	 * 
	 * @param context
	 * @param newLoginIntent
	 */
	public static void logoutAndWipeData(Context context, Boolean newLoginIntent) {
		App.logv("logoutAndWipeData");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.commit();
		logout(context, newLoginIntent);
	}

	/**
	 * Preferences
	 */
	private static final String PREFS_LOGIN = "PREFS_LOGIN";
	private static final String PREFS_TOKEN = "PREFS_TOKEN";
	private static final String PREFS_ID = "PREFS_ID";
	private static final String PREFS_IS_LOGGED_IN = "PREFS_IS_LOGGED_IN";

	
	/**
	 * get login
	 * @return
	 */
	public String getLogin() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getString(PREFS_LOGIN, null);
	}

	/**
	 * set login
	 * @param login
	 */
	public void setLogin(String login) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putString(PREFS_LOGIN, login);
		editor.commit();
	}

	/**
	 * get token
	 * @return
	 */
	public String getToken() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getString(PREFS_TOKEN, "#beerquest#");
	}
	/**
	 * 
	 * set token
	 */ 
	public void setToken(String token) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putString(PREFS_TOKEN, token);
		editor.commit();
	}

	/**
	 * getId
	 * @return
	 */
	public Long getId() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getLong(PREFS_ID, -1l);
	}

	/**
	 * setId
	 * @param id
	 */
	public void setId(Long id) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putLong(PREFS_ID, id);
		editor.commit();
	}

	/**
	 * check if user is logged
	 * @return
	 */
	public Boolean isLoggedIn() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_IS_LOGGED_IN, false);
	}

	/**
	 * 
	 * @param isLoggedIn
	 */
	public void setLoggedIn(Boolean isLoggedIn) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putBoolean(PREFS_IS_LOGGED_IN, isLoggedIn);
		editor.commit();
	}



	/**
	 * Logs
	 */
	private final static int VERBOSE = 5;
	private final static int INFORMATION = 4;
	private final static int WARNING = 3;
	private final static int DEBUG = 2;
	private final static int ERROR = 1;

	// 0 disables debug
	public final static int DEBUG_LVL = VERBOSE;
	public final static boolean DEBUG_DB = true;

	public final static String TAG = "beerquest";

	/**
	 * 
	 * @param message
	 */
	public static void logdb(String message) {
		if (DEBUG_DB && message != null)
			Log.v(TAG, message);
	}

	/**
	 * 
	 * @param message
	 */
	public static void logv(String message) {
		if (DEBUG_LVL >= 5 && message != null)
			Log.v(TAG, message);
	}

	/**
	 * 
	 * @param message
	 */
	public static void logi(String message) {
		if (DEBUG_LVL >= 4 && message != null)
			Log.v(TAG, message);
	}
	/**
	 * 
	 * @param message
	 */
	public static void logw(String message) {
		if (DEBUG_LVL >= 3 && message != null)
			Log.v(TAG, message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void logd(String message) {
		if (DEBUG_LVL >= 2 && message != null)
			Log.v(TAG, message);
	}
	
	/**
	 * 
	 * @param message
	 */
	public static void loge(String message) {
		if (DEBUG_LVL >= 1 && message != null)
			Log.v(TAG, message);
	}
	
}