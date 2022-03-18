package com.rohit.asquare.adminpanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.asquare.R
import com.rohit.asquare.adapter.MyOrdersAdapter
import com.rohit.asquare.data.CategoryOneData

class ShowAllProducts : AppCompatActivity() {
    private  lateinit var dbref: DatabaseReference
    private lateinit var categoryOneRecycler: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var itemArrayList: ArrayList<CategoryOneData>
    private lateinit var categoryOneAdapter: MyOrdersAdapter
    private lateinit var uid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_all_products_adminpanel)
        auth = FirebaseAuth.getInstance()
        val splittedlist = auth.currentUser?.email.toString().split(".")
        uid = splittedlist[0]

        itemArrayList= arrayListOf<CategoryOneData>()
        categoryOneAdapter= MyOrdersAdapter(this,itemArrayList,this@ShowAllProducts)


        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        //Recycler View1
        categoryOneRecycler=findViewById(R.id.categoryOneRecycler)
        categoryOneRecycler.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        getItemData()

    }

    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("items")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val item = userSnapshot.getValue(CategoryOneData::class.java)
                        itemArrayList.add(item!!)
                    }
                    categoryOneRecycler.adapter= categoryOneAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}