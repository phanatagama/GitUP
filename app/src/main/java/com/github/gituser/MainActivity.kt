package com.github.gituser

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)
        mainViewModel.users.observe(this, {
            setUsersData(it)
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun showSelectedUser(user: Users) {
        val moveWithDataIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                mainViewModel.users.observe(this@MainActivity, {
                    setUsersData(it)
                })
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> return true
        }
    }

    private fun setUsersData(listItems: List<ItemsItem>) {
        val listUsers = ArrayList<Users>()
        for (item in listItems) {
            val user = Users(
                item.login,
                item.avatarUrl
            )
            listUsers.add(user)
        }
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val adapter = ListUserAdapter(listUsers)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                showSelectedUser(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}