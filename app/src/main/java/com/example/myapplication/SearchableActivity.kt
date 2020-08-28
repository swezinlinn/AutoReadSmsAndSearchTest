package com.example.myapplication

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_searchable.*


@Suppress("CAST_NEVER_SUCCEEDS")
class SearchableActivity : AppCompatActivity() , SearchView.OnQueryTextListener{

    //    private val application: SenzorApplication? = null
    private var searchItemListView: ListView? = null
    private var searchView: SearchView? = null
    private var searchMenuItem: MenuItem? = null
    private var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        initFriendList();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu?.findItem(R.id.search)
        searchView = searchMenuItem?.getActionView() as SearchView

        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.setOnQueryTextListener(this@SearchableActivity)

        return super.onCreateOptionsMenu(menu)
    }

    private fun initFriendList() {
        val friendList = ArrayList<SearchItem>()

        friendList.add(SearchItem("hla hla"))
        friendList.add(SearchItem("mya hla"))
        friendList.add(SearchItem("KO KO"))
        friendList.add(SearchItem("Nyi Nyi"))
        friendList.add(SearchItem("Daw Lay"))
        friendList.add(SearchItem("Phow Phow"))
        friendList.add(SearchItem("ငွေပမာဏ"))

        searchItemListView = list_view as ListView
        searchAdapter = SearchAdapter(this, friendList)

        // add header and footer for list
        searchItemListView!!.adapter = searchAdapter
        searchItemListView!!.isTextFilterEnabled = false

        // use to enable search view popup text
        //searchItemListView.setTextFilterEnabled(true);

        // set up click listener
        searchItemListView!!.setOnItemClickListener { _, _, position, _ ->
            if (position > 0 && position <= friendList.size) {
                val categoryItem = searchAdapter!!.getItem(position - 1) as SearchItem

                Log.d("TAG","list name clicked-> ${categoryItem.name}")
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchAdapter!!.getFilter().filter(newText);
        // use to enable search view popup text
        if (TextUtils.isEmpty(newText)) {
            searchItemListView!!.clearTextFilter();
        }
        else {
            searchItemListView!!.setFilterText(newText.toString());
        }

        return true;
    }

}
