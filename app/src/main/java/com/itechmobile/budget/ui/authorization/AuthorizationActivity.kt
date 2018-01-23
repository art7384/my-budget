package com.itechmobile.budget.ui.authorization

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.itechmobile.budget.R


/**
 * Created by artem on 01.09.17.
 */
class AuthorizationActivity : AppCompatActivity() {

    companion object {
        private val LOG_TAG = "AuthorizationActivity"
        private val RC_SIGN_IN = 13
    }

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

    private lateinit var mGoogleApiClient: GoogleApiClient

    private lateinit var mSignInBt: SignInButton
    private lateinit var mEmailInBt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, {connectionResult -> } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mSignInBt = findViewById(R.id.activityAuthorization_SignInButton_signIn)
        mSignInBt.setOnClickListener { signIn() }

        mEmailInBt = findViewById(R.id.activityAuthorization_Button_emailIn)
        mEmailInBt.setOnClickListener { emailIn() }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun emailIn(){
        val intent = Intent(this, EmailAuthorizationActivity::class.java)
        startActivity(intent)
    }

    private fun signIn(){
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess)
        Log.d(LOG_TAG, "result:" + result)
        val acct = result.signInAccount
        if(acct != null) {
            with(acct){
                Log.d(LOG_TAG, "id: $id")
                Log.d(LOG_TAG, "email: $email")
                Log.d(LOG_TAG, "displayName: $displayName")
                Log.d(LOG_TAG, "givenName: $givenName")
                Log.d(LOG_TAG, "familyName: $familyName")
                Log.d(LOG_TAG, "photoUrl: $photoUrl")
                /*
                    id: 116675426022199194848
                    idToken: null
                    email: art7384@gmail.com
                    displayName: Артём Кармишин
                    givenName: Артём
                    familyName: Кармишин
                    photoUrl: null
                 */
            }

        }

        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct!!.displayName))
            //updateUI(true)
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false)
        }
    }

}