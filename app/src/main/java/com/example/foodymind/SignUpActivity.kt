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
import com.example.foodymind.databinding.ActivitySignUpBinding
import com.example.foodymind.viewmodel.SignUpViewModel
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


class SignUpActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var callbackManager: CallbackManager

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.signUpResult.observe(this) { result ->

            binding.progressBar.visibility = View.GONE
            binding.CreateAccounBtn.isEnabled = true

            if (result.first) {

                Toast.makeText(
                    this,
                    result.second,
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.loginTextInsignup.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.LoginGooglebtn.setOnClickListener {

            binding.progressBar.visibility = android.view.View.VISIBLE

            val signInIntent =
                googleSignInClient.signInIntent

            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )

        }

        binding.LoginFacebookbtn.setOnClickListener {
            binding.progressBar.visibility = android.view.View.VISIBLE
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
                    binding.progressBar.visibility = android.view.View.GONE
                }

                override fun onError(error: FacebookException) {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this@SignUpActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })


        binding.CreateAccounBtn.setOnClickListener {

            userName = binding.signUpName.text.toString().trim()
            email = binding.signUpEmail.text.toString().trim()
            password = binding.signupPassword.text.toString().trim()

            when {

                userName.isEmpty() -> {
                    binding.signUpName.error = "Enter Name"
                    binding.signUpName.requestFocus()
                }

                email.isEmpty() -> {
                    binding.signUpEmail.error = "Enter Email"
                    binding.signUpEmail.requestFocus()
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.signUpEmail.error = "Enter Valid Email"
                    binding.signUpEmail.requestFocus()
                }

                password.isEmpty() -> {
                    binding.signupPassword.error = "Enter Password"
                    binding.signupPassword.requestFocus()
                }

                password.length < 6 -> {
                    binding.signupPassword.error = "Password must be at least 6 characters"
                    binding.signupPassword.requestFocus()
                }

                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.CreateAccounBtn.isEnabled = false
                    viewModel.createAccount(
                        userName,
                        email,
                        password
                    )
                }
            }
        }
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

    private fun handleFacebookAccessToken(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = android.view.View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val userRef = database.child("user").child(user.uid)
                    userRef.get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            val newUser = userModel(
                                uid = user.uid,
                                name = user.displayName ?: "",
                                email = user.email ?: "",
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


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = android.view.View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser ?: return@addOnCompleteListener
                    val userRef = database.child("user").child(user.uid)
                    userRef.get().addOnSuccessListener { snapshot ->
                        if (!snapshot.exists()) {
                            val newUser = userModel(
                                uid = user.uid,
                                name = user.displayName ?: "",
                                email = user.email ?: "",
                                photoUrl = user.photoUrl?.toString() ?: "",
                                provider = "google"
                            )
                            userRef.setValue(newUser)
                        }
                        Toast.makeText(this, "Google Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    handleAuthError(task.exception)
                }
            }
    }

    private fun handleAuthError(exception: Exception?) {
        if (exception is FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "This email already exists. Please login using the previous method first.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, exception?.localizedMessage ?: "Authentication Failed", Toast.LENGTH_LONG).show()
        }
    }
}


