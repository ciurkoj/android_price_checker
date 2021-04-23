package com.example.pricechecker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.ProductActivity
import com.example.pricechecker.R
import com.example.pricechecker.repository.Repository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_manual.view.*
import kotlinx.android.synthetic.main.search_result.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.set


class ManualFragment : Fragment(), OnQueryTextListener {
    // TODO: Rename and change types of parameters
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var viewModel: MainActivityViewModel
    lateinit var itemsListView: ListView
    lateinit var adapter: CustomListAdapter
    private var itemsArrayList = mutableListOf<Item>() as ArrayList<Item>
    lateinit var searchBar: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manual, container, false)
        searchBar = view.findViewById<SearchView>(R.id.searchView)
        searchBar.isIconifiedByDefault = false
        searchBar.queryHint = "Search for a product name..."

        Repository.getInstance().getLiveProgress().observe(viewLifecycleOwner, Observer<String> {
            cleanList()
//            sleep(1000)
            searchBar.setQuery("$it", true)


        })
        itemsListView = view.findViewById<ListView>(R.id.searchResultsView)
//        itemsArrayList = arrayListOf<Item>()
        adapter = CustomListAdapter(context, itemsArrayList)
        itemsListView.adapter = adapter
        var listView = view.findViewById<ListView>(R.id.searchResultsView)
        listView.setPadding(0, 0, 0, 0)
        searchBar.setOnClickListener {
            searchBar.isIconifiedByDefault = true
            searchBar.isFocusable = true
            searchBar.isIconified = false
            searchBar.requestFocusFromTouch()
        }
        searchBar.setOnQueryTextListener(this)

        listView.clearFocus()
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            onQueryTextSubmit(searchBar.query.toString())
            swipeRefreshLayout.isRefreshing = false
            searchBar.clearFocus()

        }
        listView.setOnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
            searchBar.clearFocus()
            println("clear")
        }
//        view.findViewById<View>(R.id.manual_list_view)
        itemsListView.onItemClickListener = OnItemClickListener { _, view, position, _ ->
            val intent = Intent(view.context, ProductActivity::class.java)
            intent.putExtra("itemTitle", itemsArrayList[position].itemTitle)
            intent.putExtra("itemPrice", itemsArrayList[position].itemPrice)
            intent.putExtra("itemSource", itemsArrayList[position].itemSource)
            intent.putExtra("thumbnailUrl", itemsArrayList[position].thumbnailUrl)
            startActivity(intent)
        }

        return view
    }

    fun cleanList(): CustomListAdapter {
        itemsArrayList.clear()
        adapter = CustomListAdapter(context, itemsArrayList)
        adapter.notifyDataSetChanged()
        return adapter
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        itemsArrayList.clear()
        adapter = CustomListAdapter(context, itemsArrayList)
        adapter.notifyDataSetChanged()
//        TODO("Not yet implemented")
        itemsListView = requireView().findViewById<ListView>(R.id.searchResultsView)
        val searchBar = view?.findViewById<SearchView>(R.id.searchView)
        searchBar!!.setQuery(query, false)
//        val View1 = view?.findViewById<View>(R.id.manual_list_view)
//        View1?.isVisible = false
        itemsListView.clearFocus()
        swipeRefreshLayout.isRefreshing = true
        val options: HashMap<String, String> = HashMap()
        options["q"] = query
        options["google_domain"] = "google.co.uk"
        options["gl"]="uk"
        options["hl"]="en"
//        options["device"] = "desktop"
        options["tbm"] = "shop"
        options["no_cache"] = "true"
        options["tbs"] = "local_avail:1"
        options["location"] ="West Midlands, England, United Kingdom"
//        itemsArrayList = mutableListOf<Item>() as ArrayList<Item>
        val retrofit = Retrofit.Builder()
            .baseUrl("https://serpapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceHolderApi = retrofit.create(
            JsonPlaceHolderApi::class.java
        )
        val call = jsonPlaceHolderApi.getPosts(options)
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.isSuccessful) {
                    Log.e("Ddddsdad", response.toString())
                    val gson = Gson()
                    val type = object : TypeToken<BodyResponse?>() {}.type
                    val search_response = gson.fromJson<BodyResponse>(response.body(), type)
                    try {
                        for (result in search_response.shopping_results) {
                            itemsArrayList.add(
                                Item(
                                    result.resultTitle,
                                    result.resultPrice,
                                    result.resultSource,
                                    result.resultThumbnail
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            activity,
                            "Sorry, couldn't recognize the code. Please try again or use manual search",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(
                    activity,
                    "Click on an item to get more information about it.",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.e("TAG Error", t.message!!)
            }
        })
        adapter.notifyDataSetChanged()

//        val repository = Repository()
//        val viewModelFactory = MainActivityViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
//        viewModel.getCustomQuery(query, options)
//        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
//            if (response.isSuccessful) {
//
//                Log.e("dsasddasds", response.toString())
//                val gson = Gson()
//                val type = object : TypeToken<BodyResponse?>() {}.type
//                val searchResponse: BodyResponse =
//                    gson.fromJson<BodyResponse>(response.body(), type)
////                Thread.sleep(1000)
//                try {
//                    for (result in searchResponse.shopping_results) {
//                        itemsArrayList.add(
//                            Item(
//                                result.resultTitle,
//                                result.resultPrice,
//                                result.resultSource,
//                                result.resultThumbnail
//                            )
//                        )
//                    }
//                    adapter.notifyDataSetChanged()
//                    swipeRefreshLayout.isRefreshing = false
//                } catch (e: Exception) {
//                }
//            } else {
//                Toast.makeText(
//                    activity,
//                    "Sorry, couldn't recognize the code. Please try again or use manual search",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.d("Response Error: ", response.errorBody().toString())
//            }
//            adapter.notifyDataSetChanged()
//        })
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        TODO("Not yet implemented")
        return true
    }


}

