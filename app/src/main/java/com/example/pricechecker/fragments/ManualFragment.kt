package com.example.pricechecker.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.MainActivityViewModelFactory
import com.example.pricechecker.R
import com.example.pricechecker.repository.Repository
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_manual.*
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

    var refreshLayout: SwipeRefreshLayout? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    private lateinit var viewModel: MainActivityViewModel
    public var mainResponse: JsonObject? = null
    var image: String? = null
    val names = arrayListOf<String>(
        "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
        "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
        "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
        "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
        "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama"
    )


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
                println("coś jest" + k)
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
                println("nie ma nic")
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
                    println(k)
                } else if (!keys && values) {
                    print(v)
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
        val searchBar = view.findViewById<SearchView>(R.id.searchView)
        val textView = view.findViewById<TextView>(R.id.textView)
//        var imageView: ImageView = view.findViewById(R.id.imageView)
        var image2 = view.findViewById<ImageView>(R.id.imageView2)
        var listView = view.findViewById<ListView>(R.id.searchResultsView)
//        listView.adapter = MyCustomAdapter(this, names)
        println(listView.count)
        var mAdapter = MyCustomAdapter(this, names)
        listView.setAdapter(mAdapter)
        searchBar.setOnClickListener {
            searchBar.isIconifiedByDefault = true
            searchBar.isFocusable = true
            searchBar.isIconified = false
            searchBar.requestFocusFromTouch()

            if (mainResponse != null) {
                image = MyCustomAdapter(this, names).readShoppingResults(mainResponse)
            } else {
                image = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg"
            }
            println(image)
//            Glide.with(this)
//                .load(image)
//                .circleCrop()
//                .into(image2)
            textView.text = "dupa"
            listView.adapter
        }
        searchBar.setOnQueryTextListener(this)


        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            var mAdapter = MyCustomAdapter(this, names, image!!)
            listView.setAdapter(mAdapter)
            mAdapter.notifyDataSetChanged()
            println("dupsko"+image)
        }



        return view
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
//        TODO("Not yet implemented")

        textView.text = query
        println(query)
        val options: HashMap<String, String> = HashMap()
        options["google_domain"] = "google.co.uk"
        options["device"] = "desktop"
        options["tbm"] = "shop"
        options["tbs"] = "local_avail:1"
        options["location"] = "Coventry, England, United Kingdom"
        Log.e("OPTIONSSSSSSSSSSSSS ", options.toString())
        val repository = Repository()
        val viewModelFactory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.getCustomQuery(query!!.toString(), options)
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
//                readTheResponse(response, true, true)
                println(response.toString())
                textView.text = response.code().toString()
                mainResponse = response.body()
//                readShoppingResults(response)
//                Thread.sleep(2000L)
                image = MyCustomAdapter(this, names).readShoppingResults(mainResponse)
            } else {
                Log.d("Response Error: ", response.errorBody().toString())
                textView.text = response.code().toString()
            }
        })
        var listView = view?.findViewById<ListView>(R.id.searchResultsView)

        var mAdapter = MyCustomAdapter(this, names, image!!)
        listView?.setAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()
//        Thread.sleep(2000L)
//        image = MyCustomAdapter(names).readShoppingResults(mainResponse)
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
        private val names: ArrayList<String>,
        private var image: String = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg"
    ) : BaseAdapter() {

        //        var image = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg"
        fun readShoppingResults(response: JsonObject?): String {

            val gson = GsonBuilder().create()
            val type: Type = object : TypeToken<Map<String, Any>>() {}.getType()
            val map1: MutableMap<String, Any> = gson.fromJson(response, type)
            map1.forEach { (k: String, v: Any) ->
                if (v is Map<*, *>) {
                    println("coś jest" + k)
                    if (v is Map<*, *>) {
                        (v as Map<Any, Any>).forEach(BiConsumer<Any, Any> { k1: Any, v1: Any -> })
                    }
                } else {
                    println("nie ma nic" + k)

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
                                    println(value.toString())
                                    return image
                                }
                            }
                        }
                    }
                }
            }
            return image
        }


        private var mData: ArrayList<String> = names

        fun MyListAdapter(context: Any, mData: ArrayList<String>?) {
            this.mData = mData!!
            this.context = context as ManualFragment
        }

        fun getData(): ArrayList<String>? {
            return mData
        }

        // responsible for how many rows in my list
        override fun getCount(): Int {
            return names.size
        }

        // you can also ignore this
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // you can ignore this for now
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

            val search_res: View

            // checking if convertView is null, meaning we have to inflate a new row
            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(viewGroup!!.context)
                search_res = layoutInflater.inflate(R.layout.search_result, viewGroup, false)

                Log.v("getView", "calling findViewById which is expensive")
//                val nTextView = rowMain.name_textView
//                val pTextView = rowMain.position_textview
//                val nameTextView = rowMain.findViewById<TextView>(R.id.name_textView)
//                val positionTextView = rowMain.findViewById<TextView>(R.id.position_textview)
                val viewHolder = ViewHolder(
                    search_res.name_textView,
                    search_res.position_textview,
                    search_res.imageView2
                )
                search_res.tag = viewHolder

            } else {
                // well, we have our row as convertView, so just set rowMain as that view
                search_res = convertView
            }


            println("kurwaaaaa")

            val viewHolder = search_res.tag as ViewHolder
//            viewHolder.nameTextView.setTextColor(Color.WHITE)
            viewHolder.nameTextView.text = names.get(position)
            viewHolder.positionTextView.text = "Row number: $position"
            Picasso
                .get()
                .load(image)
                .resize(100, 100)
                .into(viewHolder.imageView2);

            return search_res
        }

        private class ViewHolder(
            val nameTextView: TextView,
            val positionTextView: TextView,
            val imageView2: ImageView
        )
    }



}
