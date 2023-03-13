package com.photoeditor.photoeffect

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
private lateinit var  image: ImageView
private lateinit var text:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        image=findViewById(R.id.splashImage)
        image.alpha=0f
        text=findViewById(R.id.welcome)
        text.alpha=0f
        text.animate().setDuration(2000).alpha(1f)
        image.animate().setDuration(2000).alpha(1f).withEndAction(){
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            var intent=Intent(this,SignUp_activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}