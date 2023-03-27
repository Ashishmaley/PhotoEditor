package com.photoeditor.photoeffect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.photoeditor.photoeffect.databinding.ActivityLoginBinding
import com.photoeditor.photoeffect.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_login.*

class SignUp_activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private val PASSWORD_PATTERN: Regex =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fireBaseAuth = FirebaseAuth.getInstance()
        binding.text2.setOnClickListener {
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
        }
        binding.cirLoginButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress2.text.toString()
            val pass = binding.editTextNumberPassword.text.toString()
            if (PASSWORD_PATTERN.matches(pass)) {
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    progressBar.visibility = View.VISIBLE
                    fireBaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        progressBar.visibility = View.GONE
                        if (it.isSuccessful) {
                            fireBaseAuth.currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Please verify your Email",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this, Login_Activity::class.java)
                                    startActivity(intent)
                                }
                                ?.addOnFailureListener {
                                    Toast.makeText(this, "Internet error", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Internet error or user already exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }else {
                    // Display an error message if the user does not provide an email or password
                    Toast.makeText(this, "Please fill the email and password", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                // Display an error message if the password does not meet the required restrictions
                Toast.makeText(
                    this,
                    "Password must have at least 8 characters, contain at least one uppercase letter, one lowercase letter, one number and one special character.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
        override fun onStart() {
            super.onStart()
            val currentUser = fireBaseAuth.currentUser
            if (currentUser != null) {
                if (currentUser.isEmailVerified) {
                    // User is signed in and email is verified, navigate to main activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // User is signed in but email is not verified, show message and log out
                    Toast.makeText(this, "Please verify your email to continue", Toast.LENGTH_LONG).show()
                    fireBaseAuth.signOut()
                }
            }
        }
}

