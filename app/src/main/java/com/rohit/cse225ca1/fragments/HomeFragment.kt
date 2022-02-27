package com.rohit.cse225ca1.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.MainActivity
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.catagoriesrecycler.CategoryOne
import com.rohit.cse225ca1.data.MyEquipments


class HomeFragment : Fragment() {

    private  lateinit var dbref: DatabaseReference
    private lateinit var verticalRecyclerView: RecyclerView
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2
    private lateinit var itemArrayList1: ArrayList<MyEquipments>
    lateinit var v:View

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
       v=  inflater.inflate(R.layout.fragment_home, container, false)
       val c1= v.findViewById<ImageView>(R.id.c1)
       val c2= v.findViewById<ImageView>(R.id.c2)
       val c3= v.findViewById<ImageView>(R.id.c3)
       val c4= v.findViewById<ImageView>(R.id.c4)
       //initializeClient()
       //view pager2
       viewSetPager()
       //recycler views
       setRecyclers()
       //get recyclerView Data
       getItemData()

       c1.setOnClickListener {
           //val i = Intent(this, CategoryOne::class.java)
           //startActivity(i)
       }

       return  v
    }

    private fun setRecyclers() {
        //Recycler View1
        verticalRecyclerView=v.findViewById(R.id.verticalRecycler)
        verticalRecyclerView.layoutManager=
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        itemArrayList1= arrayListOf<MyEquipments>()

        //Recycler View2
        horizontalRecyclerView=v.findViewById(R.id.horizontalRecycler)
        //horizontalRecyclerView.layoutManager=GridLayoutManager(this,2)
        horizontalRecyclerView.layoutManager=
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        //horizontalRecyclerView.adapter=MyAdapter(this@MainActivity,itemArrayList2)
    }

    //Get Data from FireBase
    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("items")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val item = userSnapshot.getValue(MyEquipments::class.java)
                        itemArrayList1.add(item!!)
                    }
                    horizontalRecyclerView.adapter= MyAdapter(this@HomeFragment,itemArrayList1)
                    verticalRecyclerView.adapter= MyAdapter(this@HomeFragment,itemArrayList1)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun viewSetPager(){
        val imagesList = listOf(
            R.drawable.bannersix,
            R.drawable.bannerfour,
            R.drawable.bannerthree,
            R.drawable.bannertwo,
            R.drawable.bannerone

        )
        val adapter = ViewPagerAdapter(imagesList)
        viewPager2 = v.findViewById(R.id.viewPager)
        viewPager2.adapter = adapter

    }



}