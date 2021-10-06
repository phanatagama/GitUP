package com.github.gituser

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()
//    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)
//        list.addAll(listUsers)
//        showRecyclerList()
    }

    private val listUsers: ArrayList<User>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataUsername = resources.getStringArray(R.array.username)
            val dataCompany = resources.getStringArray(R.array.company)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getIntArray(R.array.repository)
            val dataFollower = resources.getIntArray(R.array.followers)
            val dataFollowing = resources.getIntArray(R.array.following)
            val dataPhoto = resources.obtainTypedArray(R.array.avatar)
            val listUser = ArrayList<User>()
            for (i in dataName.indices) {
                val user = User(
                    dataUsername[i],
                    dataName[i],
                    dataPhoto.getResourceId(i, -1),
                    dataCompany[i],
                    dataLocation[i],
                    dataRepository[i],
                    dataFollower[i],
                    dataFollowing[i]
                )
                listUser.add(user)
            }
            return listUser
        }

//    private fun showRecyclerList() {
//        binding.rvUser.layoutManager = LinearLayoutManager(this)
//        val listUserAdapter = ListUserAdapter(list)
//        binding.rvUser.adapter = listUserAdapter
//
//        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: User) {
//                showSelectedUser(data)
//            }
//        })
//    }

    private fun showSelectedUser(user: User) {
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
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                findUser(query)
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
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

    private fun findUser(query: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getListUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
//                        setRestaurantData(responseBody.restaurant)
                        setUsersData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun detailUser(username: String){
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<GithubUser> {
            override fun onResponse(
                call: Call<GithubUser>,
                response: Response<GithubUser>
            ) {
//                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
//                        setRestaurantData(responseBody.restaurant)
//                        responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
//                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

//    private fun createUser(responseBody: GithubUser) {
//        val user = User(
//            responseBody.login,
//            responseBody.name.toString(),
//            responseBody.avatarUrl,
//            responseBody.company,
//            responseBody.location,
//            responseBody.publicRepos,
//            responseBody.followers,
//            responseBody.following
//        )
//        return user
//    }

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
//                showSelectedUser(data)
                Toast.makeText(this@MainActivity, data.username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}