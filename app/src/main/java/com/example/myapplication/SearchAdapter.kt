package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.collections.ArrayList
import android.widget.TextView

class SearchAdapter : BaseAdapter,Filterable{
    private var activity: SearchableActivity? = null
    private var itemFilter: FriendFilter? = null
    var itemList: ArrayList<SearchItem>? = null
    var filteredList: ArrayList<SearchItem>? = null


    constructor(activity: SearchableActivity, friendList: ArrayList<SearchItem>?){
        Log.d("TAG","frient list-> $friendList")
        this.activity = activity
        this.itemList = friendList
        filter;
    }

    @SuppressLint("ViewHolder")

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // to findViewById() on each row.
        val holder: ViewHolder
        val categoryItem = getItem(position) as SearchItem
        var itemView = view

        if (itemView == null) {
            val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = layoutInflater.inflate(R.layout.search_item, parent, false)
            holder = ViewHolder()
            holder.icon = itemView.findViewById(R.id.iv_icon) as ImageView
            holder.name = itemView.findViewById(R.id.tv_str) as TextView

            itemView.tag = holder
        } else {
            // get view holder back
            holder = itemView.tag as ViewHolder
        }

        // bind text with view holder content view for efficient use
        holder.icon!!.setImageDrawable(activity!!.resources.getDrawable(R.drawable.ic_action_search_white))
        holder.name!!.text = categoryItem.name

        return itemView!!
    }

    override fun getItem(i: Int): Any {
        return filteredList!![i];
    }

    override fun getItemId(i: Int): Long {
        return i.toLong();
    }

    override fun getCount(): Int {
        return if(filteredList != null) {
            filteredList!!.size;
        }else{
            0
        }
    }

    //get custom filters
    override fun getFilter(): Filter {
        if (itemFilter == null) {
            itemFilter = FriendFilter()
        }

        return itemFilter as FriendFilter
    }

    //view holder
    internal class ViewHolder {
        var icon: ImageView? = null
        var name: TextView? = null
    }

    //custom filter
    private inner class FriendFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.isNotEmpty()) {
                val tempList = ArrayList<SearchItem>()

                for (user in itemList!!) {
                    if (user.name.contains(constraint.toString())) {
                        tempList.add(user)
                    }
                }

                filterResults.count = tempList.size
                filterResults.values = tempList
            } else {
                filterResults.count = itemList!!.size
                filterResults.values = itemList
            }

            return filterResults
        }

       //filter result
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            filteredList = results.values as ArrayList<SearchItem>
            notifyDataSetChanged()
        }
    }

}