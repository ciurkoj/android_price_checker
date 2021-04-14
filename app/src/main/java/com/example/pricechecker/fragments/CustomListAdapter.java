package com.example.pricechecker.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pricechecker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ScanFragment context;
    private ArrayList<Item> items;


    //public constructor
    public CustomListAdapter(ScanFragment context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size(); //returns total item in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns the item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item currentItem = (Item) getItem(position);
        viewHolder.itemName.setText(currentItem.getItemName());
        viewHolder.itemDescription.setText(currentItem.getItemDescription());
        Picasso
                .get()
                .load(items.get(position).getThumbnailUrl())
                .resize(250, 250)
//                                .fit()
//                                .onlyScaleDown()
                .centerInside()
                .into(viewHolder.itemImage);

        return convertView;
    }

    //ViewHolder inner class
    private class ViewHolder {
        TextView itemName;
        TextView itemDescription;
        ImageView itemImage;

        public ViewHolder(View view) {
            itemName = (TextView)view.findViewById(R.id.name_textView);
            itemDescription = (TextView) view.findViewById(R.id.position_textview);
            itemImage = (ImageView) view.findViewById(R.id.imageView2);
        }
    }
}