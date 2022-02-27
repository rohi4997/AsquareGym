package com.rohit.cse225ca1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.authentication.GoogleSignIN
import com.rohit.cse225ca1.data.MyEquipments
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity()
{   private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private  lateinit var dbref: DatabaseReference
    private lateinit var verticalRecyclerView: RecyclerView
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2
    private lateinit var itemArrayList1: ArrayList<MyEquipments>


    override fun onCreate(savedInstanceState: Bundle?)
    {
        window.statusBarColor=this.resources.getColor(R.color.teal_200)
        val search=findViewById<EditText>(R.id.editTextTextPersonName)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //view pager2
        val imagesList = listOf(
            R.drawable.bannersix,
            R.drawable.bannerfour,
            R.drawable.bannerthree,
            R.drawable.bannertwo,
            R.drawable.bannerone

        )
        val adapter = ViewPagerAdapter(imagesList)
        viewPager2 = findViewById(R.id.viewPager)
        viewPager2.adapter = adapter



        //Recycler View1
        verticalRecyclerView=findViewById(R.id.verticalRecycler)
        verticalRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        itemArrayList1= arrayListOf<MyEquipments>()

        //Recycler View2
        horizontalRecyclerView=findViewById(R.id.horizontalRecycler)
        //horizontalRecyclerView.layoutManager=GridLayoutManager(this,2)
        horizontalRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        //horizontalRecyclerView.adapter=MyAdapter(this@MainActivity,itemArrayList2)


        getItemData()

        //Top ToolBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("A SQUARE FITNESS")
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.person_24)
        toolbar.setNavigationOnClickListener { Toast.makeText(this,"Back Arrow", Toast.LENGTH_SHORT).show() }

        //Bottom ToolBar
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
    //Get Data from FireBase
    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("items")
        dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val item = userSnapshot.getValue(MyEquipments::class.java)
                        itemArrayList1.add(item!!)
                    }
                    horizontalRecyclerView.adapter=MyAdapter(this@MainActivity,itemArrayList1)
                    verticalRecyclerView.adapter=MyAdapter(this@MainActivity,itemArrayList1)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
}