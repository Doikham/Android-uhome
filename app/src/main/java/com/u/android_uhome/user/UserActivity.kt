package com.u.android_uhome.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.u.android_uhome.R
import com.u.android_uhome.home.HomeActivity
import com.u.android_uhome.service.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        signInButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.token))
            .requestEmail()
            .build()
        googleSignInClient = getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        startService(Intent(applicationContext, FirebaseMessagingService::class.java))

        creditText.text =
            "uHome\nDevelopment team\n\n" + "Chaniporn\tWengweerakeat\n" + "Neramit\tSingh\n" + "Sethawit\tSuwincharat"
        val anim: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f
            , Animation.RELATIVE_TO_PARENT, 0f
            , Animation.RELATIVE_TO_PARENT, 0f
            , Animation.RELATIVE_TO_PARENT, 0.3f
        )

        anim.initialize(
            creditText.width, creditText.height
            , credit.width, credit.height
        )
        anim.duration = 7000
        anim.fillAfter = true
        anim.interpolator = LinearInterpolator()

        credit.visibility = View.GONE
        uhomeIcon.visibility = View.VISIBLE

        uhomeIcon.setOnClickListener {
            creditText.startAnimation(anim)
            uhomeIcon.visibility = View.GONE
            credit.visibility = View.VISIBLE
        }

        credit.setOnClickListener {
            credit.visibility = View.GONE
            uhomeIcon.visibility = View.VISIBLE
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null)
            updateUI(currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                updateUI(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        googleSignInClient.signOut()
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun revokeAccess() {
        auth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            signInButton.visibility = View.GONE
            signOutAndDisconnect.visibility = View.VISIBLE
            startApplication.visibility = View.VISIBLE
            startApplication.setOnClickListener {
                auth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result!!.token
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("token", idToken)
                        startActivity(intent)
                    } else {
                        task.exception
                    }
                }
            }
        } else {
            signInButton.visibility = View.VISIBLE
            signOutAndDisconnect.visibility = View.GONE
            startApplication.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signInButton -> {
                signIn()
            }
            R.id.signOutButton -> signOut()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}
