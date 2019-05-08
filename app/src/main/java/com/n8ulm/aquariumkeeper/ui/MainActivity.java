package com.n8ulm.aquariumkeeper.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.n8ulm.aquariumkeeper.ui.aquarium.AquariumsFragment;
import com.n8ulm.aquariumkeeper.DashboardFragment;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.TaskFragment;
import com.n8ulm.aquariumkeeper.data.User;
import com.n8ulm.aquariumkeeper.ui.log.LogFragment;
import com.n8ulm.aquariumkeeper.ui.result.ResultInputFragment;
import com.n8ulm.aquariumkeeper.ui.signin.SignInActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
		implements
		GoogleApiClient.OnConnectionFailedListener,
		NavigationView.OnNavigationItemSelectedListener,
		LogFragment.OnFragmentInteractionListener,
		ResultInputFragment.OnFragmentInteractionListener,
		DatePickerFragment.OnDateSelectedListener,
		TaskFragment.OnFragmentInteractionListener,
		AquariumsFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener {

	private static final String TAG = "MainActivity";
	public static final String ANONYMOUS = "anonymous";

	private String mUsername;

	private SharedPreferences mSharedPreferences;
	private GoogleApiClient mGoogleApiClient;



	// Firebase instance variables
	private FirebaseAuth mFirebaseAuth;
	private FirebaseUser mFirebaseUser;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseRemoteConfig mFirebaseRemoteConfig;
	private FirebaseAnalytics mFirebaseAnalytics;

	//private AdView mAdView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		// Set default username is anonymous.
		mUsername = ANONYMOUS;

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

		mFirebaseAuth = FirebaseAuth.getInstance();
		mFirebaseUser = mFirebaseAuth.getCurrentUser();
		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

		if (mFirebaseUser == null) {
			// Not signed in, launch the Sign In activity
			startActivity(new Intent(this, SignInActivity.class));
			finish();
			return;
		} else {
			mUsername = mFirebaseUser.getDisplayName();
			writeNewUser(mFirebaseUser.getUid(), mUsername, mFirebaseUser.getEmail());

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
		public boolean onCreateOptionsMenu (Menu menu){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_menu, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected (MenuItem item){
			switch (item.getItemId()) {
				case R.id.sign_out_menu:
					mFirebaseAuth.signOut();
					Auth.GoogleSignInApi.signOut(mGoogleApiClient);
					mUsername = ANONYMOUS;
					startActivity(new Intent(this, SignInActivity.class));
					finish();
					return true;
				case R.id.select_aquarium:
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}

		private void causeCrash () {
			throw new NullPointerException("Fake Null Pointer Exception");
		}

		@Override
		public void onStart () {
			super.onStart();
			// Check if user is signed in.
			// TODO: Add code to check if user is signed in.
		}

		@Override
		protected void onStop () {
			super.onStop();
		}

		@Override
		protected void onDestroy () {
			super.onDestroy();
		}

		@Override
		protected void onPause () {
			super.onPause();
		}

		@Override
		public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
			// An unresolvable error has occurred and Google APIs (including Sign-In) will not
			// be available.
			Log.d(TAG, "onConnectionFailed:" + connectionResult);
			Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFragmentInteraction (Uri uri){


		}

		@Override
		public boolean onNavigationItemSelected (@NonNull MenuItem item){
			return false;
		}

	private void writeNewUser(String userId, String name, String email) {
		User user = new User(name, email);

		mFirebaseDatabaseReference.child("users").child(userId).child("user").setValue(user);
		Log.d(TAG, "wrote new user to firebase database");
	}

	@Override
	public void onAttachFragment(@NonNull Fragment fragment) {
		super.onAttachFragment(fragment);
		if (fragment instanceof DatePickerFragment) {
			DatePickerFragment pickerFragment = (DatePickerFragment) fragment;
			pickerFragment.setOnDateSelectedListener(this);
		}
	}

	@Override
	public void onDateSelected(int year, int month, int day) {


	}

}

