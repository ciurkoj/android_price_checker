package com.example.pricechecker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pricechecker.R
import kotlinx.android.synthetic.main.row_main.view.*


class RecentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent, container, false)
        val listView = view.findViewById<ListView>(R.id.main_listview)
//        val redColor = Color.parseColor("#FF0000")
//        listView.setBackgroundColor(redColor)

        listView.adapter = MyCustomAdapter()

        // Inflate the layout for this fragment
        return view
    }

    private class MyCustomAdapter: BaseAdapter() {

//        private val mContext: Context

        private val names = arrayListOf<String>(
                "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
                "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
                "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
                "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama",
                "Donald Trump", "Steve Jobs", "Tim Cook", "Mark Zuckerberg", "Barack Obama"
        )

//        init {
//            mContext = context
//        }

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

            val rowMain: View

            // checking if convertView is null, meaning we have to inflate a new row
            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(viewGroup!!.context)
                rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)

                Log.v("getView", "calling findViewById which is expensive")
//                val nTextView = rowMain.name_textView
//                val pTextView = rowMain.position_textview
//                val nameTextView = rowMain.findViewById<TextView>(R.id.name_textView)
//                val positionTextView = rowMain.findViewById<TextView>(R.id.position_textview)
                val viewHolder = ViewHolder(rowMain.name_textView, rowMain.position_textview)
                rowMain.tag = viewHolder

            } else {
                // well, we have our row as convertView, so just set rowMain as that view
                rowMain = convertView
            }


            val viewHolder = rowMain.tag as ViewHolder
            viewHolder.nameTextView.text = names.get(position)
            viewHolder.positionTextView.text = "Row number: $position"
            return rowMain
        }

        private class ViewHolder(val nameTextView: TextView, val positionTextView: TextView)

    }

}