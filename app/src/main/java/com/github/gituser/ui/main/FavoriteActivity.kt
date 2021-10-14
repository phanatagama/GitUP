package com.github.gituser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.Database.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityFavoriteBinding
import com.github.gituser.helper.ViewModelFactory
import com.github.gituser.ui.insert.UserAddUpdateActivity

class FavoriteActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_favorite)
//    }
private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailViewModel = obtainViewModel(this@FavoriteActivity)
        detailViewModel.getAllUsers().observe(this, { userList ->
            if (userList != null) {
                adapter.setListUsers(userList)
            }
        })

        adapter = UserAdapter()
        binding?.rvUsers?.layoutManager = LinearLayoutManager(this)
        binding?.rvUsers?.setHasFixedSize(true)
        binding?.rvUsers?.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveWithDataIntent = Intent(this@FavoriteActivity, UserAddUpdateActivity::class.java)
        moveWithDataIntent.putExtra(UserAddUpdateActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}
