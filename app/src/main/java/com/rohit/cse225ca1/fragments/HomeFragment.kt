package com.rohit.cse225ca1.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.ItemDetail
import com.rohit.cse225ca1.MainActivity
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.MyAdapter
import com.rohit.cse225ca1.adapter.ViewPagerAdapter
import com.rohit.cse225ca1.catagoriesrecycler.CategoryOne
import com.rohit.cse225ca1.data.MyEquipments


class HomeFragment : Fragment() ,MyAdapter.OnClListener{

    private  lateinit var dbref: DatabaseReference
    private lateinit var popularRecycler: RecyclerView
    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var viewPager2: ViewPager2
    private lateinit var itemArrayList1: ArrayList<MyEquipments>
    lateinit var mProgressDialog:Dialog
    lateinit var v:View

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
       showProgressDialog("Please Wait")
       Log.i("HF","Home Fragment")

        // Inflate the layout for this fragment
       v=  inflater.inflate(R.layout.fragment_home, container, false)
       val c1= v.findViewById<ImageView>(R.id.c1)
       val c2= v.findViewById<ImageView>(R.id.c2)
       val c3= v.findViewById<ImageView>(R.id.c3)
       val c4= v.findViewById<ImageView>(R.id.c4)
       itemArrayList1= arrayListOf<MyEquipments>()
       //initializeClient()
       //view pager2
       viewSetPager()
       //recycler views
       setRecyclers()
       //get recyclerView Data
       getItemData()

       //mProgressDialog.dismiss()



       c1.setOnClickListener {
           val i = Intent(context, CategoryOne::class.java)
           startActivity(i)
       }
       c2.setOnClickListener {
           val i = Intent(context, CategoryOne::class.java)
           startActivity(i)
       }
       c3.setOnClickListener {
           val i = Intent(context, CategoryOne::class.java)
           startActivity(i)
       }
       c4.setOnClickListener {
           val i = Intent(context, CategoryOne::class.java)
           startActivity(i)
       }


       return  v
    }

    private fun setRecyclers() {
        //Recycler View1
        popularRecycler=v.findViewById(R.id.verticalRecycler)
        popularRecycler.layoutManager= LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        popularRecycler.adapter=MyAdapter(this@HomeFragment,itemArrayList1,this@HomeFragment)

        //Recycler View2
        horizontalRecyclerView=v.findViewById(R.id.horizontalRecycler)
        horizontalRecyclerView.layoutManager= LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        horizontalRecyclerView.adapter=MyAdapter(this@HomeFragment,itemArrayList1,this@HomeFragment)
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
                    popularRecycler.adapter= MyAdapter(this@HomeFragment,itemArrayList1,this@HomeFragment)
                    horizontalRecyclerView.adapter= MyAdapter(this@HomeFragment,itemArrayList1,this@HomeFragment)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        mProgressDialog.dismiss()
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

    override fun onItemClick(position: Int) {


        val clickedItem = itemArrayList1[position]
        //Toast.makeText(context,clickedItem.image,Toast.LENGTH_SHORT).show()
        //val bundle= Bundle()
        //val clickedItem = itemArrayList1[position]
        MyAdapter(this@HomeFragment,itemArrayList1,this@HomeFragment).notifyItemChanged(position)
        val intent=Intent(context,ItemDetail::class.java)
        intent.putExtra("name",clickedItem.title.toString())
        intent.putExtra("price",clickedItem.price.toString())
        intent.putExtra("detail",clickedItem.description)
        intent.putExtra("image",clickedItem.image)
        startActivity(intent)

    }

    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(this.requireContext(),R.style.MyDialogTheme)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        val dtv= mProgressDialog.findViewById<TextView>(R.id.textView)
        dtv.text=text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

}