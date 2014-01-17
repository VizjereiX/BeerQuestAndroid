package pl.programa.beerquest;


import org.json.JSONObject;

import pl.programa.beerquest.api.Api;
import pl.programa.beerquest.api.ApiCallback;
import pl.programa.beerquest.app.App;
import pl.programa.beerquest.utils.InternetHelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;

/**
 * class for login activity
 * @author Programa
 */
public class LoginActivity extends Activity implements
ConnectionCallbacks, OnConnectionFailedListener{
	
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

	/// UI references.
	private Button loginButton;
	private Button testApiButton;
	

	private View loginFormView;	
	private View loginStatusView;
	private TextView loginStatusMessageView;
	

	/**
	 * onCreate
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
				
		loginButton = (Button) findViewById(R.id.login_button);
		testApiButton = (Button) findViewById(R.id.apitest_button);
		
		loginFormView = findViewById(R.id.login_form);
		loginStatusView = findViewById(R.id.login_status);
		loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
        mPlusClient = new PlusClient.Builder(this, this, this)
        .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .setScopes(Scopes.PLUS_LOGIN)  // recommended login scope for social features
        // .setScopes("profile")       // alternative basic login scope
        .build();
        
        App.setMPlusClient(mPlusClient);
        
		// Progress bar to be displayed if the connection failure is not resolved.
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(InternetHelper.getInternetInfo(getApplicationContext())){
					showProgress(true);
	//				attemptLogin();
					if (view.getId() == R.id.login_button && !mPlusClient.isConnected()) {
				        if (mConnectionResult == null) {
				            mConnectionProgressDialog.show();
				        } else {
				            try {
				                mConnectionResult.startResolutionForResult(LoginActivity.this, REQUEST_CODE_RESOLVE_ERR);
				            } catch (SendIntentException e) {
				                // Try connecting again.
				                mConnectionResult = null;
				                mPlusClient.connect();
				            }
				        }
				    }
				}	
				
			}
		});
		
		testApiButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Api.sendSomething("{someobj:{testapi:1}}", getApplicationContext(),new ApiCallback() {
					@Override
					public void onResponse(Object response, Integer status, String message, Integer httpStatus) {
						App.logv("sending something to api callback");
			            if(httpStatus.equals(200)){
			            	try{
				            	App.logv("try catch sendSomething RESPONESE: " + response.toString());
					            JSONObject responseJson = (JSONObject) response;
					            //TODO do sth with data;
								Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
			            	} catch(Exception e) {
			            		App.logv("error parsing JSON");
			            	}
			            };
 					}
	    	    });
			}
		});	
	}
	
    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }
	
	/************************************ G+ **************************************************/
	@Override
	public void onConnectionFailed(ConnectionResult result) {
	       if (mConnectionProgressDialog.isShowing()) {
	               // The user clicked the sign-in button already. Start to resolve
	               // connection errors. Wait until onConnected() to dismiss the
	               // connection dialog.
	               if (result.hasResolution()) {
	                       try {
	                            result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
	                       } catch (SendIntentException e) {
	                            mPlusClient.connect();
	                       }
	               }
	       }

	       // Save the intent so that we can start an activity when the user clicks
	       // the sign-in button.
	       mConnectionResult = result;
	}	
	
	@Override
	public void onConnected(Bundle connectionHint) {
		getApp().setLoggedIn(true);
	 // We've resolved any connection errors.
	  mConnectionProgressDialog.dismiss();
	  Api.login(mPlusClient.getAccountName(), getApplicationContext(),new ApiCallback() {
			@Override
			public void onResponse(Object response, Integer status, String message, Integer httpStatus) {
				App.logv("sending login to api callback");
	            if(httpStatus.equals(200)){
	            	try{
		            	App.logv("try catch login RESPONESE: " + response.toString());
			            JSONObject responseJson = (JSONObject) response;
						Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
	            	} catch(Exception e) {
	            		App.logv("error parsing JSON");
	            	}
	            };
			}
	    });
	  Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
//
//	/**
//	 * Google login
//	 */
//	private void attemptLogin(){
//		App.logv("attemptLogin");
//		if (InternetHelper.getInternetInfo(getApplicationContext())) {
//			loginStatusMessageView.setText(R.string.login_progress_signing_in);
//			showProgress(true);
//			
//			///G+ login action here 
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//			
//			showProgress(false);
//		}		
//	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	    if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	        mConnectionResult = null;
	        mPlusClient.connect();
	    }
	}
	
    @Override
    public void onDisconnected() {
        App.logv("disconnected");
    }
	
	/*************************************************************************************************************/
	
	/**
	 * get context
	 * @return context
	 */
	private App getApp() {
		return (App) getApplicationContext();
	}	
	
	/**
	 * Shows the progress UI and hides the login form.
	 * @param show
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			loginFormView.setVisibility(View.VISIBLE);
			loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
