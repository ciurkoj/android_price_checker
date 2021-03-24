package com.example.pricechecker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pricechecker.R
import com.example.pricechecker.fragments.ManualFragment
import com.example.pricechecker.fragments.RecentFragment
import com.example.pricechecker.fragments.ScanFragment
import com.example.pricechecker.fragments.adapters.ViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.app_bar_main.*



class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel
//    val db = Firebase.firestore
//    val TAG = "<============>"
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        val user = Firebase.auth.currentUser.email
//        homeViewModel =
//                ViewModelProvider(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//
//        val drawer = inflater.inflate(R.layout.nav_header_main, container, false)
//        val udrawer: TextView = drawer.findViewById(R.id.show_username)
//        homeViewModel.text2.observe(viewLifecycleOwner, Observer {
//            udrawer.text = it
//        })
////        udrawer.text = "fddfd"
//        Log.d(TAG, "udrawer======> ${udrawer.text}")
//        val username: TextView = root.findViewById(R.id.text_username)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        Log.d(TAG, "username==> ${username.text}")
//
////        homeViewModel.text.observe(viewLifecycleOwner, Observer { username.text = "meh"})
//        db.collection("user_data")
//                .get()
//                .addOnSuccessListener { result ->
//
//                    for (document in result) {
//                        Log.d(TAG, "${document.id} => ${document.data}")
//                        homeViewModel.text1.observe(viewLifecycleOwner, Observer {
//                            username.text = user.toString()//document.data.toString()
//                        })
//                        Log.d(TAG, "===> ${username.text}")
//                        Log.d(TAG, "===> ${udrawer.text}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents.", exception)
//                }
//
//        return root
//    }
}



