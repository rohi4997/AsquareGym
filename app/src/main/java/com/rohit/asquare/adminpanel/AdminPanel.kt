package com.rohit.asquare.adminpanel

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.rohit.asquare.R
import com.rohit.asquare.authentication.GoogleSignIN
import com.rohit.asquare.fragments.CartFragment
import com.rohit.asquare.fragments.HomeFragment
import com.rohit.asquare.fragments.MoreFragment
import com.rohit.asquare.fragments.ProfileFragment

class AdminPanel : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    lateinit var mProgressDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_panel)

        auth = FirebaseAuth.getInstance()
        initializeClient()


        //Top ToolBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("A SQUARE FITNESS")
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.person_24)

        val showProduct= findViewById<ImageView>(R.id.showProducts)
        val deleteProducts= findViewById<ImageView>(R.id.deleteProducts)
        val addProducts= findViewById<ImageView>(R.id.addProducts)
        val allUsers= findViewById<ImageView>(R.id.allUsers)

        showProduct.setOnClickListener {
            val intent = Intent(this,ShowAllProducts::class.java)
            startActivity(intent)
        }
        deleteProducts.setOnClickListener {
            val intent = Intent(this,DeleteProduct::class.java)
            startActivity(intent)
        }
        addProducts.setOnClickListener {
            val intent = Intent(this,AddProduct::class.java)
            startActivity(intent)
        }
        allUsers.setOnClickListener {
            val intent = Intent(this,AllUsers::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }

    //option Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int =item.itemId
        if(id==R.id.logout){
            signOut()
            val intnt = Intent(this, GoogleSignIN::class.java)
            startActivity(intnt)
            finish()
            Toast.makeText(applicationContext,"Logout SuccessFul", Toast.LENGTH_LONG).show()}
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

    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        val dtv= mProgressDialog.findViewById<TextView>(R.id.textView)
        dtv.text=text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }
}