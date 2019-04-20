package com.n8ulm.aquariumkeeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

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

		// Assign Fields
		mSignInButton = findViewById(R.id.sign_in_button);

		// Set click listeners
		mSignInButton.setOnClickListener(this);

		// Configure Google Sign In
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		// Initialize FirebaseAuth
		mFirebaseAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.sign_in_button:
				signIn();
				break;
		}

	}

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// Google Sign-In was successful, authenticate with Firebase
				GoogleSignInAccount account = result.getSignInAccount();
				firebaseAuthWithGoogle(account);
			} else {
				// Google Sign-In failed
				Log.d(TAG, "Google Sign-In Failed");
			}
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		mFirebaseAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Log.w(TAG, "signInWithCredential", task.getException());
							Toast.makeText(SignInActivity.this, "Authentication Failed",
									Toast.LENGTH_SHORT).show();
						} else {
							startActivity(new Intent(SignInActivity.this, MainActivity.class));
							finish();
						}
					}
				});
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
	// An unresolvable error has occurred and Google APIs (including Sign-In) will not
	// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}
}
