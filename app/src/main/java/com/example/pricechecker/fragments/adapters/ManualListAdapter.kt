package com.example.pricechecker.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pricechecker.R
import com.example.pricechecker.fragments.Item
import com.example.pricechecker.fragments.ManualFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_result.view.*
import java.util.*


class ManualListAdapter(private val context: ManualFragment, private val items: ArrayList<Item>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size //returns total item in the list
    }

    override fun getItem(position: Int): Any {
        return items[position] //returns the item at the specified position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ManualListAdapter.ViewHolder
        if (convertView == null) {
            convertView =
                LayoutInflater.from(parent.context).inflate(R.layout.search_result, parent, false)
            viewHolder = ViewHolder(
                    convertView.item_title,
                convertView.item_price,
                convertView.item_source,
                convertView.item_thumbnail
            )
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ManualListAdapter.ViewHolder
        }
        val currentItem = getItem(position) as Item
        viewHolder.itemTitle.text = currentItem.itemTitle
        viewHolder.itemPrice.text = currentItem.itemPrice
        viewHolder.itemSource.text = currentItem.itemSource
        Picasso
            .get()
            .load(items[position].thumbnailUrl)
            .resize(250, 250) //                                .fit()
            //                                .onlyScaleDown()
            .centerInside()
            .into(viewHolder.itemThumbnail)
        return convertView
    }

    //ViewHolder inner class
    private class ViewHolder(
        val itemTitle: TextView,
        val itemPrice: TextView,
        val itemSource: TextView,
        val itemThumbnail: ImageView
    )

}