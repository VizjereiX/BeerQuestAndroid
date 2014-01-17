package pl.programa.beerquest;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;

import pl.programa.beerquest.app.App;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	double lat;
	double lng;
	Button logoutButton;
	PlusClient mPlusClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		App.logv("Main activity on create");
		App.logv(ConnectionResult.SUCCESS + " succ " + GooglePlayServicesUtil.isGooglePlayServicesAvailable(App.getContext())); 
		super.onCreate(savedInstanceState);

		//authorize();

		// get G+ client
		mPlusClient = App.getMPlusClient();
<<<<<<< HEAD

=======
		if(mPlusClient == null){
		    getApp().setLoggedIn(false);
		    authorize();
		}
			
		
>>>>>>> fbccdcc6ccd8874be0725d4bbdcd242ed8cca091
		setContentView(R.layout.activity_main);

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
<<<<<<< HEAD
=======
		
		
>>>>>>> fbccdcc6ccd8874be0725d4bbdcd242ed8cca091
		
		newQuestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, NewQuestActivity.class);
				startActivity(intent);
			}
		});

		// GPS
		enableGPS();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			lng = location.getLongitude();
			lat = location.getLatitude();
		}

		// location change listener
		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				lng = location.getLongitude();
				lat = location.getLatitude();
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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

}
