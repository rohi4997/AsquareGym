package com.rohit.cse225ca1.catagoriesrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.CategoryOneAdapter
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.data.MyEquipments

class CategoryOne : AppCompatActivity() {
    private  lateinit var dbref: DatabaseReference
    private lateinit var categoryOneRecycler: RecyclerView
    private lateinit var itemArrayList: ArrayList<MyEquipments>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_one)

        //Recycler View1
        categoryOneRecycler=findViewById(R.id.categoryOneRecycler)
        categoryOneRecycler.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        itemArrayList= arrayListOf<MyEquipments>()
        getItemData()


    }

    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("items")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val item = userSnapshot.getValue(MyEquipments::class.java)
                        itemArrayList.add(item!!)
                    }
                    categoryOneRecycler.adapter= CategoryOneAdapter(this@CategoryOne,itemArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}