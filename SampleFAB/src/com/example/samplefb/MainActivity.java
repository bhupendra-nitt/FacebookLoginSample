package com.example.samplefb;



import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class MainActivity extends Activity {
	private static String APP_ID = " Replace your App ID here"; 
	// Instance of Facebook Class
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	Button btnFbLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
	

	
	btnFbLogin = (Button) findViewById(R.id.button1);
	btnFbLogin.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	            loginToFacebook();
	        }
	});
	}
	public void loginToFacebook() {

		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
			
			btnFbLogin.setVisibility(View.INVISIBLE);
			
			// Making get profile button visible
		///	btnFbGetProfile.setVisibility(View.VISIBLE);

			// Making post to wall visible
			//btnPostToWall.setVisibility(View.VISIBLE);

			// Making show access tokens button visible
			//btnShowAccessTokens.setVisibility(View.VISIBLE);

			Log.i("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							// Making Login button invisible
							btnFbLogin.setVisibility(View.INVISIBLE);

							// Making logout Button visible
						//	btnFbGetProfile.setVisibility(View.VISIBLE);

							// Making post to wall visible
							//btnPostToWall.setVisibility(View.VISIBLE);

							// Making show access tokens button visible
							//btnShowAccessTokens.setVisibility(View.VISIBLE);
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
