package com.rohit.cse225ca1
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setTitle("A SQUARE FITNESS")
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.heart)
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this,"Back Arrow", Toast.LENGTH_SHORT).show()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    Toast.makeText(this,"menu_home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_notification -> {
                    Toast.makeText(this,"menu_notification", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_search -> {
                    Toast.makeText(this,"menu_search", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_profile -> {
                    Toast.makeText(this,"menu_profile", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }








    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int =item.itemId
        if(id==R.id.toast) {
            val vg: ViewGroup? = findViewById(R.id.custom_toast)
            val inflater = layoutInflater
            val layout: View = inflater.inflate(R.layout.custom_view_group,vg)
            val tv = layout.findViewById<TextView>(R.id.txtVw)
            tv.text = "You want to exit?"
            val toast = Toast(applicationContext)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0 , 300)
            toast.duration = Toast.LENGTH_LONG
            toast.setView(layout)
            toast.show()
        }
        else if(id==R.id.rate){
            Toast.makeText(applicationContext,"Settings",Toast.LENGTH_LONG).show()
            intent = Intent(this, RatingBar::class.java)
            startActivity(intent)
        }
        else if(id==R.id.like){Toast.makeText(applicationContext,"Like",Toast.LENGTH_LONG).show()}
        return super.onOptionsItemSelected(item)
    }


}