package com.n8ulm.aquariumkeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView mTextMessage;

	private SharedPreferences mSharedPreferences;
	private GoogleApiClient mGoogleApiClient;

	// Firebase instance variables
	private FirebaseAuth mFirebaseAuth;
	private FirebaseUser mFirebaseUser;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseRemoteConfig mFirebaseRemoteConfig;
	private FirebaseAnalytics mFirebaseAnalytics;

	private AdView mAdView;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					mTextMessage.setText(R.string.title_home);
					return true;
				case R.id.navigation_dashboard:
					mTextMessage.setText(R.string.title_dashboard);
					return true;
				case R.id.navigation_notifications:
					mTextMessage.setText(R.string.title_notifications);
					return true;
			}
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		// Set default username is anonymous.
		//mUsername = ANONYMOUS;

		mTextMessage = (TextView) findViewById(R.id.message);
		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

		mFirebaseAuth = FirebaseAuth.getInstance();
		mFirebaseUser = mFirebaseAuth.getCurrentUser();
		if (mFirebaseUser == null) {
			// Not signed in, launch the Sign In activity
			startActivity(new Intent(this, SignInActivity.class));
			finish();
			return;
		} else {
			//mUsername = mFirebaseUser.getDisplayName();
			//if (mFirebaseUser.getPhotoUrl() != null) {
			//	mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
			//}
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API)
				.build();

		// Initialize Firebase Remote Config.
		mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

		// Define Firebase Remote Config Settings.
		FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
				new FirebaseRemoteConfigSettings.Builder()
						.setDeveloperModeEnabled(true)
						.build();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in.
		// TODO: Add code to check if user is signed in.
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
