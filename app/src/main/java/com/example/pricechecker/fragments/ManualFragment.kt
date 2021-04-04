package com.example.pricechecker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.pricechecker.MainActivityViewModel
import com.example.pricechecker.MainActivityViewModelFactory
import com.example.pricechecker.R
import com.example.pricechecker.repository.Repository
import kotlinx.android.synthetic.main.fragment_manual.*

class ManualFragment : Fragment(), OnQueryTextListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: MainActivityViewModel
    private var mainResponse : String? = null

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val options: HashMap<String, String> = HashMap()
        options["google_domain"] = "google.co.uk"
        options["device"] = "mobile"
        Log.e("OPTIONSSSSSSSSSSSSS ", options.toString())
        val repository = Repository()
        val viewModelFactory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.getCustomQuery("milk", options)
        viewModel.myResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.e("Response=====>: ", response.toString())
                Log.e("Response=====>: ", response.body()?.search_metadata.toString())
                Log.d("Response: ", response.body()?.search_parameters.toString())
                Log.e("Response=====>: ", response.body()?.search_metadata.toString())
                Log.d("Response: ", response.body()?.recipes_results.toString())
                Log.e("Response=====>: ", response.body()?.local_map.toString())
                Log.d("Response: ", response.body()?.knowledge_graph.toString())
                Log.e("Response=====>: ", response.body()?.related_questions.toString())
                Log.d("Response: ", response.body()?.organic_results.toString())
                Log.e("Response=====>: ", response.body()?.related_search_boxes.toString())
                Log.d("Response: ", response.body()?.related_searches.toString())
                Log.e("Response=====>: ", response.body()?.pagination.toString())
                Log.d("Response: ", response.body()?.serpapi_pagination.toString())

                textView.text = response.code().toString()
                mainResponse = response.body()?.search_metadata.toString()
            } else {
                Log.e("Response=====>: ", response.toString())
                Log.d("Response: ", response.errorBody().toString())
                textView.text = response.code().toString()
            }
        })

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manual, container, false)
        val searchBar = view.findViewById<SearchView>(R.id.searchView)
        val textView = view.findViewById<TextView>(R.id.textView)
        val thiscontext = container?.context

        searchBar.setOnClickListener {
            searchBar.isIconifiedByDefault = true
            searchBar.isFocusable = true
            searchBar.isIconified = false
            searchBar.requestFocusFromTouch()
            textView.text = mainResponse
            val url ="https://serpapi.com/search.json?q=jeans+buy&location=Austin%2C+Texas%2C+United+States&hl=en&gl=us&api_key=55eb0b1549172ab8e0ef83026fdc56fce225517f8006d98c78766c218f3217aa"

        }
        searchBar.setOnQueryTextListener(this)
        return view
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
//        TODO("Not yet implemented")

        textView.text = "duupaaa!!!"
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        TODO("Not yet implemented")
        if (newText != null) {
            textView.text = newText.toString()
        }
        return true
    }


    fun sendRequest() {
// ...
        Log.i(
            "======> API ====> ",
            "apiiii"
        )
// Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
        val url =
            "https://serpapi.com/search.json?q=jeans+buy&location=Austin%2C+Texas%2C+United+States&hl=en&gl=us&api_key=55eb0b1549172ab8e0ef83026fdc56fce225517f8006d98c78766c218f3217aa"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
            { textView.text = "That didn't work!" })

// Add the request to the RequestQueue.
//        queue.add(stringRequest)
//        return
    }

}

