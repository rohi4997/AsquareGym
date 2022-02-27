package com.rohit.cse225ca1
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rohit.cse225ca1.authentication.GoogleSignIN

class SplashScreen : AppCompatActivity()
{    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val i = Intent(this,GoogleSignIN::class.java)
                startActivity(i)
                finish()},2200)
    }
}
