package com.example.pricechecker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.ProductActivity
import com.example.pricechecker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RecentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var itemsArrayList = mutableListOf<Item>() as ArrayList<Item>
    private val viewModel: MainActivityViewModel by activityViewModels()
    var db = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    val recent_searches = db.collection("/user_data/${user.email}/recent_searches")
    lateinit var adapter: CustomListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dummyData = hashMapOf(
            "itemTitle" to "Ada",
            "itemPrice" to "Lovelace",
            "itemSource" to 1815,
            "thumbnailUrl" to "thumbnailUrl"
        )
        db.collection("user_data").whereEqualTo(user.email, user.email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.size() > 0) {
                        for (document in task.result) {
                            Log.e("FTAG", "Room already exists, start the chat")
                        }
                    } else {
                        db.collection("user_data/${user.email}/recent_searches")
                            .document("dummy")
                            .set(dummyData)
                            .addOnSuccessListener { documentReference ->
                            Log.e("TAG", "DocumentSnapshot added with ID: $documentReference")
                        }
                            .addOnFailureListener { e ->
                                Log.e("TAG", "Error adding document", e)
                            }

                        Log.e("FTAG", "room doesn't exist create a new room")

                    }
                } else {
                    Log.e("FTAG", "Error getting documents: ", task.exception)
                }
            }
        db.collection("user_data/${user.email}/recent_searches")
            .document("dummy")
            .delete()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent, container, false)
        val itemsListView = view.findViewById<ListView>(R.id.main_listview)
        itemsArrayList = mutableListOf<Item>() as ArrayList<Item>
        itemsArrayList.clear()

        adapter = CustomListAdapter(context, itemsArrayList)
        adapter.clearData()
        recent_searches
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {

                        itemsArrayList.add(
                            0, Item(
                                document.data!!.getValue("itemTitle").toString(),
                                document.data!!.getValue("itemPrice").toString(),
                                document.data!!.getValue("itemSource").toString(),
                                document.data!!.getValue("thumbnailUrl").toString()
                            )
                        )
                    }catch (e: Exception){

                    }
                }
                itemsListView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error getting documents: ", exception)
            }
        itemsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->

                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("itemTitle", itemsArrayList[position].itemTitle)
                intent.putExtra("itemPrice", itemsArrayList[position].itemPrice)
                intent.putExtra("itemSource", itemsArrayList[position].itemSource)
                intent.putExtra("thumbnailUrl", itemsArrayList[position].thumbnailUrl)
                val temp = itemsArrayList[position]
                itemsArrayList[position] = itemsArrayList[0]
                itemsArrayList[0] = temp
                startActivity(intent)
            }
        return view
    }


    override fun onPause() {
        super.onPause()
        adapter.notifyDataSetChanged()
    }
}