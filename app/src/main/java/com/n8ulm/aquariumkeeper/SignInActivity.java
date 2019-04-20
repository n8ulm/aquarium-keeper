package com.n8ulm.aquariumkeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements
		GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

	private static final String TAG = "SignInActivity";
	private static final int RC_SIGN_IN = 9001;

	private SignInButton mSignInButton;

	private GoogleApiClient mGoogleApiClient;

	// Firebase instance variables
	private FirebaseAuth mFirebaseAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		mSignInButton = findViewById(R.id.sign_in_button);

		// Configure Google Sign In
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}
