package pl.programa.beerquest.interfaces;

import pl.programa.beerquest.LoginActivity;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Custom Activity with custom action bar and useful methods
 * 
 * @author Programa.pl
 * 
 */
public abstract class ActionBarActivity extends FragmentActivity{

	///parameters
	Button actionBarLogout;
	TextView actionBarTitle;
	ProgressBar spinner;
	
	Toast toast;	
	


	/**
	 * onCreate
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		actionBarLogout = (Button) findViewById(R.id.actionBarLogout);
		actionBarTitle = (TextView) findViewById(R.id.actionBarTitle);

		
		actionBarLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				App.logv("logout clicked");
				App.logoutClose(ActionBarActivity.this);
				Intent intent = new Intent(ActionBarActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});	
	}

	/**
	 * set activity's custom bar title
	 * 
	 * @param title
	 */
	public void setCustomTitle(String title) {
		App.logv("set TItle: " + title);
		actionBarTitle = (TextView) findViewById(R.id.actionBarTitle);
		actionBarTitle.setText(title);
	}


	/**
	 * Get App singleton
	 * 
	 * @return App
	 */
	protected App getApp() {
		return (App) getApplicationContext();
	}

}