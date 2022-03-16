package com.rohit.asquare

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PaymentSingleItem : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var bundle: Bundle
    private lateinit var myText: String
    //private lateinit var address: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_single_item)
        auth = FirebaseAuth.getInstance()
        val splittedlist = auth.currentUser?.email.toString().split(".")
        uid = splittedlist[0]
        bundle = intent.extras!!
        myText="Hi Admin, \n"+
                "This Is ${bundle?.get("Name").toString()}\n" +
                "I would Like to buy the product ${bundle?.get("name").toString()} from your app\n"+
                "Price: ${bundle?.get("price").toString()}\n" +
                "Address: ${bundle?.get("Village").toString()}\n"+
                "Mobile: ${bundle?.get("Mobile").toString()}\n"+
                "Payment:"
//                "${bundle?.get("addr").toString()}"


        val nme= findViewById<TextView>(R.id.name)
        val vlg= findViewById<TextView>(R.id.vlg)
        val mobl= findViewById<TextView>(R.id.mobl)
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener { onBackPressed() }
        nme.text=bundle?.get("Name").toString()
        //nme.setText(bundle?.get("Name").toString())
        vlg.setText(bundle?.get("Village").toString())
        mobl.setText(bundle?.get("Mobile").toString())

        val btnDeliver = findViewById<Button>(R.id.deliverr)
        btnDeliver.setOnClickListener {
            dialogPaymentOpt()
        }
    }


    private fun dialogPaymentOpt(){
        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder
            .setMessage("Select The Payment Method")
            .setCancelable(true)
        // .setMessage("this is alert")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.WHITE)
        layout.setPadding(15, 0, 15, 0)
        val cashOnDelivery = Button(this)
        val payOnline = Button(this)
        cashOnDelivery.text = "Cash On Delivery"
        payOnline.text = "Pay Online Using UPI"
        layout.addView(cashOnDelivery)
        layout.addView(payOnline)
        builder.setView(layout)

        payOnline.setOnClickListener {
            initPayment()
        }
        cashOnDelivery.setOnClickListener {
            //Toast.makeText(this, "Cashondelivery", Toast.LENGTH_SHORT).show()
            //builder.setCancelable(true)
            //clearCartAddHistory()
            areYouSureDialog()


        }

        builder.setPositiveButton("Cancel") { dialogInterface, which ->
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun areYouSureDialog() {
        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder
            .setMessage("Are You Sure You Want to place This Order")
            .setCancelable(false)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            //Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG).show()
            initMsg()

        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
        }

        val alertDialog:AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun initMsg() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
            //premission is here
            sendMsg()
            successDialog()
        }else{
            //request for permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),100)

        }
    }

    private fun sendMsg() {
        var obj = SmsManager.getDefault()
        obj.sendTextMessage("6375376225",null,myText, null,null)
    }

    fun initPayment() {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", "8875565063@ybl")
            .appendQueryParameter("pn", "A Square Gym")
            .appendQueryParameter("tn", "Purchased from Asquare")
            .appendQueryParameter("am", bundle?.get("price").toString())
            .appendQueryParameter("cn", "INR").build()

        val upiIntent = Intent(Intent.ACTION_VIEW)
        upiIntent.setData(uri)
        val chooser = Intent.createChooser(upiIntent, "Pay With");
        if (this?.let { chooser.resolveActivity(it.packageManager) } != null) {
            startActivityForResult(chooser, 100)
        } else {
            Toast.makeText(this, "No paying Apps", Toast.LENGTH_SHORT).show()
        }
        //Toast.makeText(context,"In Init Payment",Toast.LENGTH_SHORT).show()
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

    private fun upiPaymentDataOperation(datalist: ArrayList<String>) {
        if (isConnectionAvailabe(this)) {

            var str: String = datalist.get(0)
            Log.d("UPI", "upiPaymentDataOperation " + str)
            var paymentCancel: String = ""
            if (str == null) str = "discard"
            var status: String = ""
            var approvalRefNo: String = ""
            val response: List<String> = str.split("&")
            for (i in response) {
                val equalStr: List<String> = str.split("=")
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) status =
                        equalStr[1].toLowerCase()
                    else if (equalStr[0].toLowerCase()
                            .equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase()
                            .equals("txnRef".toLowerCase())
                    ) approvalRefNo = equalStr[1]
                } else paymentCancel = "payment cancelled by user."
            }

            if (status.equals("success")) {
                Log.d("UPI", "responseStr: " + approvalRefNo)
                myText=myText+"DONE"
                initMsg()
                addHistory()
                successDialog()

            } else if (paymentCancel.equals("payment cancelled by user.")) {
                Toast.makeText(this, "payment cancelled", Toast.LENGTH_SHORT).show()
                //addHistory()

            } else {
                Toast.makeText(this, "Transaction Failed. Plaease Try later", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "No InterNet Connection", Toast.LENGTH_SHORT).show()
        }


    }

    private fun addHistory(){
        dbref = FirebaseDatabase.getInstance().getReference()
        dbref.child("users").child(uid).child("userOrderHistory").child(bundle?.get("name").toString()).child("name").setValue(bundle?.get("name").toString())
        dbref.child("users").child(uid).child("userOrderHistory").child(bundle?.get("name").toString()).child("price").setValue(bundle?.get("price").toString())
        dbref.child("users").child(uid).child("userOrderHistory").child(bundle?.get("name").toString()).child("description").setValue(bundle?.get("detail").toString())
        dbref.child("users").child(uid).child("userOrderHistory").child(bundle?.get("name").toString()).child("image").setValue(bundle?.get("image").toString())
        dbref.child("users").child(uid).child("userOrderHistory").child(bundle?.get("name").toString()).child("deliveryAddress").setValue(bundle?.get("addr").toString())

    }

    private fun isConnectionAvailabe(context: Context?): Boolean {
        //val connectivityManager:ConnectivityManager=ConnectivityManager.getSystemService(Context.CONNECTIVITY_SERVICE)
        return true
    }

    private fun successDialog() {
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage("Order Successfully Placed")
            .setCancelable(false)
// .setMessage("this is alert")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.WHITE)
        layout.setPadding(15, 0, 15, 0)
        val imgsuccess = pl.droidsonroids.gif.GifImageView(this)
        imgsuccess.setImageResource(R.drawable.successcircle)
        val continueShopping = Button(this)
        continueShopping.text = "Continue To Home"
        //val trackOrder = Button(this)
        //trackOrder.text = "Track Order"
        layout.addView(imgsuccess)
        layout.addView(continueShopping)
        //layout.addView(trackOrder)
        builder.setView(layout)

        continueShopping.setOnClickListener {
            builder.setCancelable(true)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
//        trackOrder.setOnClickListener {
//            builder.setCancelable(true)
//            val intent = Intent(this, OrderDetailsStatus::class.java)
//            startActivity(intent)
//            finish()
//        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100 && grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //when permission is granted
            //sendMessage
            sendMsg()
            successDialog()

        }else{
            //when permission is denied
            Toast.makeText(this,"Please Allow The Permission in Settings",Toast.LENGTH_SHORT).show()
        }
    }
}