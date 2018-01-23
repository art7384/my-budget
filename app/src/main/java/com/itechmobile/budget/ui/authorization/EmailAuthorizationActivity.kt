package com.itechmobile.budget.ui.authorization

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.itechmobile.budget.R


/**
 * Created by artem on 12.09.17.
 */
class EmailAuthorizationActivity : AppCompatActivity() {

    companion object {
        private val LOG_TAG = "EmailAuthorizationActivity"
    }

    private lateinit var mEmailEt: EditText
    private lateinit var mPasswordEt: EditText
    private lateinit var mOkBt: Button
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_authorization)

        mEmailEt = findViewById(R.id.activityEmailAuthorization_EditText_email)
        mPasswordEt = findViewById(R.id.activityEmailAuthorization_EditText_password)
        mOkBt = findViewById(R.id.activityEmailAuthorization_Button_ok)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    @SuppressLint("LongLogTag")
    private fun signIn(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(LOG_TAG, "signInWithEmail:success")
                val user = mAuth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(LOG_TAG, "signInWithEmail:failure", task.getException())
                Toast.makeText(this@EmailAuthorizationActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun updateUI(user: FirebaseUser?){
        if (user != null) {
            with(user) {
                Log.d(LOG_TAG, "name: $displayName")
                Log.d(LOG_TAG, "email: $email")
                Log.d(LOG_TAG, "photoUrl: $photoUrl")
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                Log.d(LOG_TAG, "uid: $uid")
            }

        }
    }

}