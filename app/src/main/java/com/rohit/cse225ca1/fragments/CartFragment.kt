package com.rohit.cse225ca1.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rohit.cse225ca1.R
import com.rohit.cse225ca1.adapter.CartAdapter
import com.rohit.cse225ca1.data.MyCartItems
import java.lang.reflect.Array

class CartFragment : Fragment(),CartAdapter.OnClListener {
    private  lateinit var dbref: DatabaseReference
    private lateinit var cartRecycler: RecyclerView
    private lateinit var itemArrayList: ArrayList<MyCartItems>
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val v = inflater.inflate(R.layout.fragment_cart, container, false)
        auth = FirebaseAuth.getInstance()
        val splittedlist = auth.currentUser?.email.toString().split(".")
        uid = splittedlist[0]
        itemArrayList = arrayListOf<MyCartItems>()
        cartAdapter = CartAdapter(this@CartFragment, itemArrayList, this@CartFragment)

        //Recycler View1
        cartRecycler = v.findViewById(R.id.myCart)
        cartRecycler.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        cartRecycler.adapter=cartAdapter
        getItemData()

        val totalAmount = v.findViewById<TextView>(R.id.totalAmount)
        val placeButton = v.findViewById<Button>(R.id.place)

        placeButton.setOnClickListener {
            if (itemArrayList.isNullOrEmpty()) {
                Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show()
            }else{


                goPurchase()
                //initPayment()
            }
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            100->
                if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
                    if (data!=null)
                    {
                        //Toast.makeText(context,data.toString(),Toast.LENGTH_SHORT).show()
                        val trxt=data.getStringExtra("response");
                        Log.d("UPI","onActivityResult"+trxt)
                        val datalist= ArrayList<String> ()
                        datalist.add(trxt.toString())
                        upiPaymentDataOperation(datalist)
                    }else
                    {
                        Log.d("UPI","onActivityResult"+"Return data is null")
                        val datalist= ArrayList<String> ()
                        datalist.add("nothing")
                        upiPaymentDataOperation(datalist)
                    }
                }
                else
                {
                    Log.d("UPI","onActivityResult"+"Return data is null")
                    val datalist= ArrayList<String> ()
                    datalist.add("nothing")
                    upiPaymentDataOperation(datalist)
                }
        }

    }

    override fun onItemClick(position: Int) {
            if (itemArrayList.isNotEmpty()){
                if (position<0){
                    Toast.makeText(context, "less than 0", Toast.LENGTH_SHORT).show()
                }
                else {
                    val clickedItem = itemArrayList[position]
                    Toast.makeText(context, clickedItem.name, Toast.LENGTH_SHORT).show()
                    dbref = FirebaseDatabase.getInstance().getReference()
                    dbref.child("users").child(uid).child("usercart").child(clickedItem.name.toString())
                        .setValue(null)

                    itemArrayList.remove(itemArrayList[position])
                    cartAdapter.notifyItemChanged(position)
                    cartRecycler.adapter=cartAdapter
                    //refreshFragment()
                }
            }
        }

    fun refreshFragment() {
            context?.let {
                val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
                fragmentManager?.let {
                    val currentFragment = fragmentManager.findFragmentById(R.id.frameLayout)
                    currentFragment?.let {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.detach(it)
                        fragmentTransaction.attach(it)
                        fragmentTransaction.commit()
                    }
                }
            }
        }

    fun initPayment(){
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa","8875565063@ybl")
            .appendQueryParameter("pn","A Square Gym")
            .appendQueryParameter("tn","Purchased from Asquare")
            .appendQueryParameter("am","100")
            .appendQueryParameter("cn","INR").build()

        val upiIntent= Intent(Intent.ACTION_VIEW)
        upiIntent.setData(uri)
        val chooser = Intent.createChooser(upiIntent,"Pay With");
        if (context?.let { chooser.resolveActivity(it.packageManager) } !=null){
            startActivityForResult(chooser,100)
        }else{
            Toast.makeText(context,"No paying Apps",Toast.LENGTH_SHORT).show()
        }
        //Toast.makeText(context,"In Init Payment",Toast.LENGTH_SHORT).show()
    }

    private fun upiPaymentDataOperation(datalist: ArrayList<String>) {
        if (isConnectionAvailabe(context)){

            var str:String=datalist.get(0)
            Log.d("UPI","upiPaymentDataOperation "+str)
            var paymentCancel:String = ""
            if (str==null) str="discard"
            var status:String = ""
            var approvalRefNo:String = ""
            val response: List<String> = str.split("&")
            for (i in response){
                val equalStr: List<String> = str.split("=")
                if(equalStr.size>=2){
                    if(equalStr[0].toLowerCase().equals("Status".toLowerCase())) status=equalStr[1].toLowerCase()
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) approvalRefNo=equalStr[1]
                }else paymentCancel="payment cancelled by user."
            }

            if (status.equals("success")){
                dbref = FirebaseDatabase.getInstance().getReference()
                dbref.child("users").child(uid).child("usercart").setValue(null)
                itemArrayList.removeAll(itemArrayList)
                cartAdapter.notifyDataSetChanged()
                cartRecycler.adapter=cartAdapter
                Toast.makeText(context,"Transaction Successful",Toast.LENGTH_SHORT).show()
                Log.d("UPI","responseStr: "+approvalRefNo)
            }
            else if (paymentCancel.equals("payment cancelled by user.")){
                Toast.makeText(context,"payment cancelled by user.",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Transaction Failed. Plaease Try later",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context,"No InterNet Connection",Toast.LENGTH_SHORT).show()
        }

    }

    private fun isConnectionAvailabe(context: Context?): Boolean {
        //val connectivityManager:ConnectivityManager=ConnectivityManager.getSystemService(Context.CONNECTIVITY_SERVICE)
        return true
    }

    private fun goPurchase() {
        val builder = AlertDialog.Builder(context,R.style.MyDialogTheme)
        builder
            .setMessage("Select The Payment Method")
            .setCancelable(true)
        // .setMessage("this is alert")
        val layout = LinearLayout(context)
        layout.orientation= LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.WHITE)
        layout.setPadding(15,0,15,0)
        val cashOnDelivery = Button(context)
        val payOnline = Button(context)
        cashOnDelivery.text="Cash On Delivery"
        payOnline.text="Pay Online Using UPI"
        layout.addView(cashOnDelivery)
        layout.addView(payOnline)
        builder.setView(layout)

        payOnline.setOnClickListener {
            initPayment()
        }
        cashOnDelivery.setOnClickListener {
            Toast.makeText(context,"Cashondalivery",Toast.LENGTH_SHORT).show()
            //builder.setCancelable(true)
        }

        builder.setPositiveButton("Cancel") { dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun getItemData() {
        dbref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("usercart")
        //dbref = FirebaseDatabase.getInstance().getReference("items")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val item = userSnapshot.getValue(MyCartItems::class.java)
                        itemArrayList.add(item!!)
                    }
                    cartRecycler.adapter = cartAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}