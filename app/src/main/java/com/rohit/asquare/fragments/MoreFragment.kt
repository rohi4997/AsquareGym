package com.rohit.asquare.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.rohit.asquare.MyOrders
import com.rohit.asquare.R
import com.rohit.asquare.activities.AboutUs
import com.rohit.asquare.activities.ContactUs
import com.rohit.asquare.activities.Faqs
import com.rohit.asquare.activities.TermsConditions

class MoreFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_more, container, false)
        val userName= v.findViewById<TextView>(R.id.userName)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        userName.text=user?.displayName

        val myOrders=v.findViewById<TextView>(R.id.myOrders)
        val contactUs=v.findViewById<TextView>(R.id.contactUs)
        val aboutUs=v.findViewById<TextView>(R.id.aboutUs)
        val rateUs=v.findViewById<TextView>(R.id.rateUs)
        val shareApp=v.findViewById<TextView>(R.id.shareApp)
        val faQ=v.findViewById<TextView>(R.id.faQ)
        val termsCond=v.findViewById<TextView>(R.id.termsCond)


        myOrders.setOnClickListener {
            val intent = Intent(this.context,MyOrders::class.java)
            startActivity(intent)
        }
        contactUs.setOnClickListener {
            val intent = Intent(this.context,ContactUs::class.java)
            startActivity(intent)
        }
        aboutUs.setOnClickListener {
            val intent = Intent(this.context,AboutUs::class.java)
            startActivity(intent)
        }
        rateUs.setOnClickListener {
//            val intent = Intent(this.context,RatingBar::class.java)
//            startActivity(intent)
        }
        faQ.setOnClickListener {
            val intent = Intent(this.context,Faqs::class.java)
            startActivity(intent)
        }
        termsCond.setOnClickListener {
            val intent = Intent(this.context,TermsConditions::class.java)
            startActivity(intent)
        }

//        shareApp.setOnClickListener {
//            val intent = Intent(this.context,MyOrders::class.java)
//            startActivity(intent)
//        }

        return v
    }

}