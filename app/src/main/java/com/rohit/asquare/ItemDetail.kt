package com.rohit.asquare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ItemDetail : AppCompatActivity() {
    private  lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        val title = findViewById<TextView>(R.id.title)
        val price = findViewById<TextView>(R.id.price)
        val description = findViewById<TextView>(R.id.description)
        val image = findViewById<ImageView>(R.id.image)
        val back = findViewById<ImageView>(R.id.back)
        val button = findViewById<Button>(R.id.button)
        val crt:String="usercart"
        val buyBtn= findViewById<Button>(R.id.buy)

        dbref = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val splittedlist = auth.currentUser?.email.toString().split(".")
        val uid=splittedlist[0]

        val bundle:Bundle? = intent.extras
        title.text=bundle?.get("name").toString()
        price.text=bundle?.get("price").toString()
        description.text=bundle?.get("detail").toString()
        Glide.with(this).load(bundle?.get("image").toString()).into(image)

        back.setOnClickListener {
            onBackPressed()
        }

        button.setOnClickListener {

            dbref.child("users").child(uid).child(crt).child(bundle?.get("name").toString()).child("name").setValue(bundle?.get("name").toString())
            dbref.child("users").child(uid).child(crt).child(bundle?.get("name").toString()).child("price").setValue(bundle?.get("price").toString())
            dbref.child("users").child(uid).child(crt).child(bundle?.get("name").toString()).child("description").setValue(bundle?.get("detail").toString())
            dbref.child("users").child(uid).child(crt).child(bundle?.get("name").toString()).child("image").setValue(bundle?.get("image").toString())

            Toast.makeText(this,"Added to Cart",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        buyBtn.setOnClickListener {
            val intent = Intent(this,AddAddress::class.java)
            intent.putExtra("name",bundle?.get("name").toString())
            intent.putExtra("price",bundle?.get("price").toString())
            intent.putExtra("detail",bundle?.get("detail").toString())
            intent.putExtra("image",bundle?.get("image").toString())
            startActivity(intent)
        }


    }
}