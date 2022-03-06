package com.rohit.cse225ca1.catagoriesrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.CategoryOneAdapter
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.data.CategoryOneData
import com.rohit.cse225ca1.data.MyEquipments

class CategoryOne : AppCompatActivity() ,CategoryOneAdapter.OnClListener{
    private  lateinit var dbref: DatabaseReference
    private lateinit var categoryOneRecycler: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var itemArrayList: ArrayList<CategoryOneData>
    private lateinit var categoryOneAdapter: CategoryOneAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_one)
        auth = FirebaseAuth.getInstance()

        itemArrayList= arrayListOf<CategoryOneData>()
        categoryOneAdapter=CategoryOneAdapter(this@CategoryOne,itemArrayList,this@CategoryOne)


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

    override fun onItemClick(position: Int) {
        val clickedItem = itemArrayList[position]
        Toast.makeText(this,clickedItem.title,Toast.LENGTH_SHORT).show()
        val splittedlist = auth.currentUser?.email.toString().split(".")
        val uid=splittedlist[0]
        dbref = FirebaseDatabase.getInstance().getReference()
        dbref.child("users").child(uid).child("usercart").child(clickedItem.title.toString()).child("name").setValue(clickedItem.title.toString())
        dbref.child("users").child(uid).child("usercart").child(clickedItem.title.toString()).child("price").setValue(clickedItem.price.toString())
        dbref.child("users").child(uid).child("usercart").child(clickedItem.title.toString()).child("description").setValue(clickedItem.description.toString())
        dbref.child("users").child(uid).child("usercart").child(clickedItem.title.toString()).child("image").setValue(clickedItem.image.toString())


    }
}