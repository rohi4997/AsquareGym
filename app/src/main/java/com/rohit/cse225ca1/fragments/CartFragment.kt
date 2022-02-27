package com.rohit.cse225ca1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.CartAdapter
import com.rohit.cse225ca1.data.MyEquipments

class CartFragment : Fragment() {
    private  lateinit var dbref: DatabaseReference
    private lateinit var cartRecycler: RecyclerView
    private lateinit var itemArrayList: ArrayList<MyEquipments>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_cart, container, false)
        //Recycler View1
        cartRecycler=v.findViewById(R.id.myCart)
        cartRecycler.layoutManager=
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        itemArrayList= arrayListOf<MyEquipments>()
        getItemData()

        return v
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
                    cartRecycler.adapter= CartAdapter(this@CartFragment,itemArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}