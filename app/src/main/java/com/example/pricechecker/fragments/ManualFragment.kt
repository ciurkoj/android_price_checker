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
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.MainActivityViewModelFactory
import com.example.pricechecker.ProductActivity
import com.example.pricechecker.R
import com.example.pricechecker.model.ExampleItem
import com.example.pricechecker.repository.Repository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_manual.view.*
import kotlinx.android.synthetic.main.search_result.view.*
import retrofit2.Response
import java.lang.reflect.Type
import java.util.function.BiConsumer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


class ManualFragment : Fragment(), OnQueryTextListener {
    // TODO: Rename and change types of parameters
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var viewModel: MainActivityViewModel
    lateinit var itemsListView: ListView
    lateinit var adapter: CustomListAdapter
    private var itemsArrayList = mutableListOf<Item>() as ArrayList<Item>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manual, container, false)
        val searchBar = view.findViewById<SearchView>(R.id.searchView)
        searchBar.isIconifiedByDefault = false
        searchBar.queryHint = "Search for a product name..."

        Repository.getInstance().getLiveProgress().observe(viewLifecycleOwner, Observer<String> {
            itemsArrayList.clear()
            adapter = CustomListAdapter(context, itemsArrayList)
            adapter.notifyDataSetChanged()

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
            onQueryTextSubmit(searchBar.query as String)
            swipeRefreshLayout.isRefreshing = false
            searchBar.clearFocus()

        }
        listView.setOnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
            searchBar.clearFocus()
            println("clear")
        }
        view.findViewById<View>(R.id.manual_list_view)
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


    override fun onQueryTextSubmit(query: String): Boolean {
//        TODO("Not yet implemented")
        var listView = view?.findViewById<ListView>(R.id.searchResultsView)
        val searchBar = view?.findViewById<SearchView>(R.id.searchView)
        searchBar!!.setQuery(query, false)
        val View1 = view?.findViewById<View>(R.id.manual_list_view)
        View1?.isVisible = false
        listView?.clearFocus()
        swipeRefreshLayout.isRefreshing = true
        itemsArrayList.clear()
        adapter = CustomListAdapter(context, itemsArrayList)
        adapter.notifyDataSetChanged()
        val options: HashMap<String, String> = HashMap()
        options["google_domain"] = "google.co.uk"
        options["device"] = "desktop"
        options["tbm"] = "shop"
        options["no_cache"] = "true"
        options["tbs"] = "local_avail:1"
        options["location"] = "Coventry, England, United Kingdom"
        val repository = Repository()
        val viewModelFactory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.getCustomQuery(query, options)
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {

                    Log.e("dsasddasds", response.toString())
                    val gson = Gson()
                    val type = object : TypeToken<BodyResponse?>() {}.type
                    val searchResponse: BodyResponse =
                        gson.fromJson<BodyResponse>(response.body(), type)
                    Thread.sleep(1000)
                    try {
                        for (result in searchResponse.shopping_results) {
                            itemsArrayList.add(
                                Item(
                                    result.resultTitle,
                                    result.resultPrice,
                                    result.resultSource,
                                    result.resultThumbnail
                                )
                            )
                        }
                        adapter.notifyDataSetChanged()
                        swipeRefreshLayout.isRefreshing = false
                    } catch (e: Exception) {
                }

            } else {
                Toast.makeText(
                    activity,
                    "Sorry, couldn't recognize the code. Please try again or use manual search",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("Response Error: ", response.errorBody().toString())
            }
        })
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        TODO("Not yet implemented")
        return true
    }


}
