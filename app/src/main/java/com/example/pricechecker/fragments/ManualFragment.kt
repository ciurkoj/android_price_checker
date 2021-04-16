package com.example.pricechecker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.MainActivityViewModelFactory
import com.example.pricechecker.R
import com.example.pricechecker.model.ExampleItem
import com.example.pricechecker.repository.Repository
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import kotlinx.android.synthetic.main.row_main.view.name_textView
import kotlinx.android.synthetic.main.row_main.view.position_textview
import kotlinx.android.synthetic.main.search_result.view.*
import retrofit2.Response
import java.lang.reflect.Type
import java.util.function.BiConsumer


class ManualFragment : Fragment(), OnQueryTextListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var viewModel: MainActivityViewModel
    public var mainResponse: JsonObject? = null
    var image: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


    }


    fun readTheResponse(response: Response<JsonObject>, keys: Boolean, values: Boolean) {
        val gson = GsonBuilder().create()
        val type: Type = object : TypeToken<Map<String, Any>>() {}.getType()
        val map1: MutableMap<String, Any> = gson.fromJson(response.body(), type)
        map1.forEach { (k: String, v: Any) ->
            if (v is Map<*, *>) {
//                println("coś jest" + k)
                if (keys && values) {
//                    println("$k => $v")
                    if (v is Map<*, *>) {
                        (v as Map<Any, Any>).forEach(BiConsumer<Any, Any> { k1: Any, v1: Any -> })
                    }
                } else if (keys && !values) {
//                    println(k)
                } else if (!keys && values) {
//                    print(v)
                    if (v is Map<*, *>) {
                        (v as Map<Any, Any>).forEach(BiConsumer<Any, Any> { k1: Any, v1: Any -> })
                    }
                }
            } else {
//                println("nie ma nic")
                if (keys && values) {
                    if (k == "shopping_results") {
//                        println("$k => $v")
                        if (k != "top_stories_link" && k != "top_stories_serpapi_link") {
//                            println(k)
                            val x: List<Any> = v as List<Any>
                            for (value in x) {
                                val value1 = value as Map<*, *>
                                value1.forEach { (key, value) ->
//                                    if (key == "position" && value == "1.0") {
//                                        println("$key = $value")
//                                    }
                                }
                            }
                            val v1 = x[0] as Map<*, *>
                            v1.forEach { (key, value) ->
                                if (key == "thumbnail") {
                                    image = value.toString()
                                }
                            }
                        }
                    }
                } else if (keys && !values) {
//                    println(k)
                } else if (!keys && values) {
//                    print(v)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manual, container, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val searchBar = view.findViewById<SearchView>(R.id.searchView)
        searchBar.isIconifiedByDefault = false

        searchBar.queryHint = "Search for a product name..."

        println("stataataaart")
//        searchBar.setQuery("coffee", false)
        var image2 = view.findViewById<ImageView>(R.id.item_thumbnail)
        var listView = view.findViewById<ListView>(R.id.searchResultsView)
        listView.setPadding(0, 0, 0, 0)
        searchBar.setOnClickListener {
            searchBar.isIconifiedByDefault = true
            searchBar.isFocusable = true
            searchBar.isIconified = false
            searchBar.requestFocusFromTouch()

            textView.text = "dupa"
        }
        searchBar.setOnQueryTextListener(this)

        listView.clearFocus()
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            searchBar.clearFocus()


        }
        listView.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            searchBar.clearFocus()
            println("clear")
        }

        var View1 = view.findViewById<View>(R.id.View1)

        View1.setOnClickListener {
            searchBar.clearFocus()
            println("clear")
        }


        return view
    }


    override fun onQueryTextSubmit(query: String): Boolean {
//        TODO("Not yet implemented")
        var listView = view?.findViewById<ListView>(R.id.searchResultsView)
        val searchBar = view?.findViewById<SearchView>(R.id.searchView)
        searchBar!!.setQuery(query, false)
        val View1 = view?.findViewById<View>(R.id.View1)
        View1?.isVisible = false
        listView?.clearFocus()
        swipeRefreshLayout.isRefreshing = true
        textView.text = query
        println(query)
        val options: HashMap<String, String> = HashMap()
        options["google_domain"] = "google.co.uk"
        options["device"] = "desktop"
        options["tbm"] = "shop"
        options["no_cache"] = "true"
        options["tbs"] = "local_avail:1"
        options["location"] = "Coventry, England, United Kingdom"
        Log.e("OPTIONSSSSSSSSSSSSS ", options.toString())
        val repository = Repository()
        val viewModelFactory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.getCustomQuery(query!!.toString(), options)
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                println(response)
                mainResponse = response.body()
                var mAdapter = MyCustomAdapter(this, mainResponse!!, swipeRefreshLayout)
                listView?.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
            } else {
                Log.d("Response Error: ", response.errorBody().toString())
            }
        })
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        TODO("Not yet implemented")
        if (newText != null) {
            textView.text = newText.toString()
        }
        return true
    }

    private class MyCustomAdapter(
        var context: ManualFragment,
        private val jsonData: JsonObject? = null,
        var swipeRefreshLayout: SwipeRefreshLayout,
        private var image: String = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg"
    ) : BaseAdapter() {
        private var mData: JsonObject = jsonData!!
        fun MyListAdapter(context: Any, mData: JsonObject) {
            this.mData = mData
            this.context = context as ManualFragment
        }

        fun getData(): JsonObject {
            return mData
        }

        // responsible for how many rows in my list
        override fun getCount(): Int {
            var size: Int = 0
            val gson = GsonBuilder().create()
            val type: Type = object : TypeToken<Map<String, Any>>() {}.getType()
            val map1: MutableMap<String, Any> = gson.fromJson(jsonData, type)
            map1.forEach { (k: String, v: Any) ->
                if (k == "shopping_results") {
                    v as List<Any>
                    size = v.size

                }
            }

            return size
        }

        // you can also ignore this
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // you can ignore this for now
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        fun readShoppingResults(response: JsonObject?): String {
            val gson = GsonBuilder().create()
            val type: Type = object : TypeToken<Map<String, Any>>() {}.getType()
            val map1: MutableMap<String, Any> = gson.fromJson(response, type)
            map1.forEach { (k: String, v: Any) ->

                if (v is Map<*, *>) {
//                    println("coś jest" + k)
                    if (v is Map<*, *>) {
                        (v as Map<Any, Any>).forEach(BiConsumer<Any, Any> { k1: Any, v1: Any -> })
                    }
                } else {
                    if (k == "shopping_results") {
                        if (k != "top_stories_link" && k != "top_stories_serpapi_link") {
                            val x: List<Any> = v as List<Any>
                            for (value in x) {
                                val value1 = value as Map<*, *>
                                value1.forEach { (key, value) ->
                                }
                            }
                            val v1 = x[0] as Map<*, *>
                            v1.forEach { (key, value) ->
                                if (key == "thumbnail") {
                                    val image = value.toString()
//                                    println(value.toString())
                                    return image
                                }
                            }
                        }
                    }
                }
            }
            return image
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val search_res: View

            // checking if convertView is null, meaning we have to inflate a new row
            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(viewGroup!!.context)
                search_res = layoutInflater.inflate(R.layout.search_result, viewGroup, false)
                val viewHolder = ViewHolder(
                    search_res.item_title,
                    search_res.item_price,
                    search_res.item_source,
                    search_res.item_thumbnail
                )
                search_res.tag = viewHolder
            } else {
                // well, we have our row as convertView, so just set rowMain as that view
                search_res = convertView
            }
            val viewHolder = search_res.tag as ViewHolder
            val gson = GsonBuilder().create()
            val type: Type = object : TypeToken<Map<String, Any>>() {}.getType()
            val map1: MutableMap<String, Any> = gson.fromJson(jsonData, type)
            println(map1)
            map1.forEach { (k: String, v: Any) ->

                when (v) {
                    is String -> println("String: 'v'. Capitalize :{v.capitalize()}")
                    is List<*> -> println("Float: 'v'")
                    is ArrayList<*> -> println("Double: 'v'")
                    is Object -> println("Integer: 'v'")
                    else -> println("Unknown Type")
                }
                if (v is Map<*, *>) {
                    if (v is Map<*, *>) {
//                        println(v)
                        (v as Map<Any, Any>).forEach(BiConsumer<Any, Any> { k1: Any, v1: Any ->
//                            println("$k1 : $v1")
                        })
                    }
                } else {
                    if (k == "shopping_results") {

                        val x = v as List<Any>
                        var result: Map<Any, Any> = x[position] as Map<Any, Any>
                        result.getValue("title").toString()
                            .also { viewHolder.itemTitle.text = it }
                        result.getValue("price").toString()
                            .also { viewHolder.itemPrice.text = it }
                        result.getValue("source").toString()
                            .also { viewHolder.itemSource.text = it }
//                                println("====>"+position)
                        result.getValue("thumbnail").also {
                            Picasso
                                .get()
                                .load(it.toString())
                                .resize(250, 250)
//                                .fit()
//                                .onlyScaleDown()
                                .centerInside()
                                .into(viewHolder.itemThumbnail);
                        }
                    }
                }
            }

            if (notifyDataSetChanged() != null) {
                swipeRefreshLayout.isRefreshing = false
            }
            return search_res
        }


        private class ViewHolder(
            val itemTitle: TextView,
            val itemPrice: TextView,
            val itemSource: TextView,
            val itemThumbnail: ImageView
        )
    }

    private class ExampleAdapter(private val exampleList: List<ExampleItem>) :
        RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.search_result,
                parent, false
            )

            return ExampleViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
            val currentItem = exampleList[position]
            holder.itemThumbnail.setImageResource(currentItem.imageResource)
            holder.textView.text = currentItem.text1
        }

        override fun getItemCount() = exampleList.size

        class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val itemThumbnail: ImageView = itemView.item_thumbnail
            val textView: TextView = itemView.textView

        }
    }

    interface AdapterCallback {
        fun itemsBound()
    }

}
