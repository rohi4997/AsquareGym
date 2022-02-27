package com.rohit.cse225ca1

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.authentication.GoogleSignIN
import com.rohit.cse225ca1.catagoriesrecycler.CategoryOne
import com.rohit.cse225ca1.data.MyEquipments
import com.rohit.cse225ca1.fragments.CartFragment
import com.rohit.cse225ca1.fragments.HomeFragment
import com.rohit.cse225ca1.fragments.MoreFragment
import com.rohit.cse225ca1.fragments.ProfileFragment
import java.util.*


class MainActivity : AppCompatActivity()
{
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    lateinit var homeFragmant:HomeFragment
    lateinit var profileFragmant: ProfileFragment
    lateinit var cartFragmant: CartFragment
    lateinit var moreFragmant: MoreFragment


    override fun onCreate(savedInstanceState: Bundle?)
    {
        val search=findViewById<EditText>(R.id.editTextTextPersonName)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeFragmant = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,homeFragmant)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        window.statusBarColor=this.resources.getColor(R.color.teal_200)


        auth = FirebaseAuth.getInstance()
        initializeClient()


        //Top ToolBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("A SQUARE FITNESS")
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.person_24)
        toolbar.setNavigationOnClickListener { Toast.makeText(this,"Back Arrow", Toast.LENGTH_SHORT).show() }

        //Bottom ToolBar
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            //Fragment selectedItem=null
            when (it.itemId) {
                R.id.menu_home -> {
                    Toast.makeText(this,"Home", Toast.LENGTH_SHORT).show()
                    homeFragmant = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,homeFragmant)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    true
                }
                R.id.profile -> {
                    profileFragmant = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,profileFragmant)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    true
                }
                R.id.cart -> {
                    cartFragmant = CartFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,cartFragmant)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    true
                }
                R.id.menu_profile -> {
                    moreFragmant = MoreFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,moreFragmant)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
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

    //option Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int =item.itemId
        if(id==R.id.toast) {
            Toast.makeText(applicationContext,"Settings",Toast.LENGTH_LONG).show()
        }
        else if(id==R.id.rate){
            Toast.makeText(applicationContext,"Settings",Toast.LENGTH_LONG).show()
            intent = Intent(this, RatingBar::class.java)
            startActivity(intent)
        }
        else if(id==R.id.logout){
            signOut()
            val intnt = Intent(this,GoogleSignIN::class.java)
            startActivity(intnt)
            finish()
            Toast.makeText(applicationContext,"Logout SuccessFul",Toast.LENGTH_LONG).show()}
        return super.onOptionsItemSelected(item)
    }

    fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }

    private fun initializeClient() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}