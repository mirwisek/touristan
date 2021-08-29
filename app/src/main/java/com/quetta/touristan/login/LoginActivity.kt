package com.quetta.touristan.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.quetta.touristan.ui.MainActivity
import com.quetta.touristan.R
import com.quetta.touristan.toast

class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN: Int = 10101
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private var rootView: View? = null

    override fun onStart() {
        super.onStart()

        // If signed in already then skip the login activity
        if(GoogleSignIn.getLastSignedInAccount(this) != null) {
            startMainActivity()
        }
//        FirebaseAuth.getInstance().currentUser?.let {
//            startMainActivity()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignIn = findViewById<SignInButton>(R.id.btnLogin)

        auth = FirebaseAuth.getInstance()
        rootView = findViewById(R.id.parent)

        btnSignIn.setOnClickListener {
            signIn()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_signin_token))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthGoogle(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    displayError(task.exception!!)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user == null) {
            toast("User is null")
            finish()
        } else {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthGoogle(account!!)
                } catch (e: ApiException) {
                    displayError(e)
                }
            }
        }
    }

    private fun displayError(e: Exception) {
        rootView?.let {
            Snackbar.make(it, "Login Error: ${e.message}", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}