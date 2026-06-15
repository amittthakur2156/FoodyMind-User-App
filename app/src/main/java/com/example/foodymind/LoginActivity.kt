package com.example.foodymind

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodymind.Model.userModel
import com.example.foodymind.databinding.ActivityLoginBinding
import com.example.foodymind.viewmodel.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var callbackManager: CallbackManager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        database = Firebase.database.reference
        callbackManager = CallbackManager.Factory.create()

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(
            this,
            gso
        )
        setContentView(binding.root)
        viewModel.loginResult.observe(this) { result ->

            binding.progressBar.visibility = View.GONE

            if (result.first) {

                Toast.makeText(
                    this,
                    result.second,
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )

                finish()

            } else {

                Toast.makeText(
                    this,
                    result.second,
                    Toast.LENGTH_LONG
                ).show()

            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.loginTextInsignup.setOnClickListener {
            val intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.LoginGooglebtn.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            val signInIntent =
                googleSignInClient.signInIntent

            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )

        }

        binding.LoginBtn.setOnClickListener {

            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Show progress bar if it exists in layout
            binding.progressBar.visibility = View.VISIBLE

            viewModel.login(
                email,
                password
            )

        }
        binding.resetPassword.setOnClickListener {

            val email =
                binding.Email.text.toString().trim()

            if (email.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter email",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        "Reset link sent",
                        Toast.LENGTH_LONG
                    ).show()

                }
                .addOnFailureListener {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()

                }

        }


        binding.LoginFacebookbtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                callbackManager,
                listOf("email", "public_profile")
            )
        }
         LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken.token)
                }
                override fun onCancel() {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(error: FacebookException) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val userRef = database.child("user").child(user.uid)
                    userRef.get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            val newUser = userModel(
                                uid = user.uid,
                                name = user.displayName ?: "",
                                email = user.email ?: "",
                                password = "",
                                photoUrl = user.photoUrl?.toString() ?: "",
                                provider = "google"
                            )
                            userRef.setValue(newUser)
                        }
                        Toast.makeText(
                            this,
                            "Google Login Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    handleAuthError(task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val userRef = database.child("user").child(user.uid)
                    userRef.get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            val newUser = userModel(
                                uid = user.uid,
                                name = user.displayName ?: "",
                                email = user.email ?: "",
                                password = "",
                                photoUrl = user.photoUrl?.toString() ?: "",
                                provider = "facebook"
                            )
                            userRef.setValue(newUser)
                        }
                        Toast.makeText(this, "Facebook Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    handleAuthError(task.exception)
                }
            }
    }
    private fun handleAuthError(exception: Exception?) {
        binding.progressBar.visibility = View.GONE
        if (exception is FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "This email already exists. Please login using the previous method first.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, exception?.localizedMessage ?: "Authentication Failed", Toast.LENGTH_LONG).show()
        }
    }
}