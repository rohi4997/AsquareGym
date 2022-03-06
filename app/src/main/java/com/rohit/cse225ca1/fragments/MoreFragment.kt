package com.rohit.cse225ca1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.rohit.cse225ca1.R

class MoreFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_more, container, false)
        val userName= v.findViewById<TextView>(R.id.userName)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        userName.text=user?.displayName

        return v
    }

}