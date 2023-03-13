package com.photoeditor.photoeffect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.photoeditor.photoeffect.databinding.ActivityLoginBinding
import com.photoeditor.photoeffect.databinding.ActivitySignUpBinding

class SignUp_activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireBaseAuth: FirebaseAuth
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
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                fireBaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, Login_Activity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "please fill both the required field", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(fireBaseAuth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}
