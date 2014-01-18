package pl.programa.beerquest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;


import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;


import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.model.Quest;
import pl.programa.beerquest.model.QuestList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	double lat;
	double lng;
	Button logoutButton;
	PlusClient mPlusClient;
	Quest[] que;
	ArrayList<Quest> questList;	
	ArrayList<String> list;
	ListView listview;
	WebView webView;
	
	public static final String SENDER_ID = "461172195817";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		App.logv("Main activity on create");
		super.onCreate(savedInstanceState);

		// get G+ client
		mPlusClient = App.getMPlusClient();
		if (mPlusClient == null) {
			getApp().setLoggedIn(false);
			 authorize();
		}

		setContentView(R.layout.activity_main);
		listview = (ListView) findViewById(R.id.listview);
		listview.setVisibility(View.GONE);
		// init logout
		logoutButton = (Button) findViewById(R.id.actionBarLogout);
		Button newQuestButton = (Button) findViewById(R.id.actionBarNewQuest);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPlusClient.isConnected()) {
					mPlusClient.clearDefaultAccount();
					mPlusClient.disconnect();
					Toast.makeText(getApplicationContext(), "logout",
							Toast.LENGTH_SHORT).show();
				}
				getApp().setLoggedIn(false);
				authorize();
			}
		});

        
        //init webview
        webView = (WebView) findViewById(R.id.main_web_view);
        webView.setWebViewClient(new WebViewClient());
        //enable control JavaScript from app
        webView.addJavascriptInterface(new WebAppInterface(MainActivity.this), "Android");
        //handle user link clicks in thesame webview
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); 
//        webView.loadUrl("http://autostrada.z.dev.programa.pl/map");
        
        //enable JavaScript
   


		newQuestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						NewQuestActivity.class);
				startActivity(intent);
			}
		});

		// GPS
		enableGPS();
		registerToGCM();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			lng = location.getLongitude();
			lat = location.getLatitude();
			Api.locationChange(lat, lng, App.getContext());
		}

		// location change listener
		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				lng = location.getLongitude();
				lat = location.getLatitude();
				Api.locationChange(lat, lng, App.getContext());
			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};

		// map refresh
		final MyTimerTask myTask = new MyTimerTask();
		final Timer myTimer = new Timer();
		myTimer.schedule(myTask, App.MILISEC_MAP_REFRESH,
				App.MILISEC_MAP_REFRESH);

		// /LIST
		questList = new ArrayList<Quest>();

		Api.getQuests("{}", getApplicationContext(), new ApiCallback() {

			@Override
			public void onResponse(Object response, Integer status,
					String message, Integer httpStatus) {
				App.logv("sending something to api callback");; 
				//if (httpStatus.equals(200)) {
					try {
						App.logv("try catch get quests RESPONESE: "
								+ response.toString());
						QuestList qList = new Gson().fromJson(
								response.toString(), QuestList.class);
						que = qList.getListQuests();
						App.logv("QUEST length ------> " + que.length);

						Toast.makeText(getApplicationContext(),
								response.toString(), Toast.LENGTH_SHORT).show();

					} catch (Exception e) {
						App.logv("error parsing JSON ges Quests");
					}
					//ArraList values = new ArrayList();
					String[] values = new String[que.length];
					for (int i = 0; i < que.length; i++) {
						questList.add(que[i]);
						values[i] = que[i].getName();
					}
					list = new ArrayList<String>();
					for (int i = 0; i < values.length; ++i) {
						list.add(values[i]);
					}
				//}

				listview.setVisibility(View.VISIBLE);

				StableArrayAdapter adapter = new StableArrayAdapter(MainActivity.this,
						android.R.layout.simple_list_item_1, list);
				listview.setAdapter(adapter);

				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, final View view,
							int position, long id) {
						App.setQ(que[position]);
						Toast.makeText(getApplicationContext(), "quest saved: " + id,
								Toast.LENGTH_SHORT).show();
						Intent qInfoIntent = new Intent(MainActivity.this,
								QuestInfoActivity.class);
						startActivity(qInfoIntent);
					}

				});
			}
		});


	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * authorize
	 */
	private void authorize() {
		if (!getApp().isLoggedIn()) {
			App.logi("MainTabActivity AUTHORIZATION FAILED");
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableGPS();
	}

	private void enableGPS() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		sendBroadcast(intent);
	}

	private void disableGPS() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", false);
		sendBroadcast(intent);
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// send lat and lng by API
					// get other users and refresh map
				}
			});
		}
	}

	/**
	 * get context
	 * 
	 * @return context
	 */
	private App getApp() {
		return (App) getApplicationContext();
	}

	private void registerToGCM() {
		if (Build.VERSION.SDK_INT >= 8) {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			String regId = GCMRegistrar.getRegistrationId(this);
			App.logv("GCM: " + regId);
			if (regId.equals("")) {
				GCMRegistrar.register(this, SENDER_ID);
			}
		}
	}

}
