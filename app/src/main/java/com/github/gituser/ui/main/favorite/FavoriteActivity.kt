package com.github.gituser.ui.main.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.R
import com.github.gituser.databinding.ActivityFavoriteBinding
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.ui.common.showToast
import com.github.gituser.ui.main.ListUserAdapter
import com.github.gituser.ui.main.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
//    private val binding get() = _activityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
//    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        observe()

//        val detailViewModel = obtainViewModel(this@FavoriteActivity)
//        detailViewModel.getAllUsers().observe(this, { userList ->
//            if (userList != null) {
//                adapter.setListUsers(userList)
//            }
//        })
//
//        adapter = UserAdapter()
//        binding?.rvUsers?.layoutManager = LinearLayoutManager(this)
//        binding?.rvUsers?.setHasFixedSize(true)
//        binding?.rvUsers?.adapter = adapter
//
//        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: UserEntity) {
//                showSelectedUser(data)
//            }
//        })
    }

    private fun observe() {
        favoriteViewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
                state -> handleStateChanges(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleStateChanges(state: FavoriteActivityState) {
        when(state){
            is FavoriteActivityState.Init -> Unit
            is FavoriteActivityState.IsSuccess -> handleSuccess(state.userEntity)
            is FavoriteActivityState.IsLoading -> handleLoading(state.isLoading)
            is FavoriteActivityState.IsError -> handleError(state.message)
        }

    }

    private fun handleError(message: String) {
        return showToast(message)
    }
    private fun setupRecyclerView(){
        val adapter = ListUserAdapter(mutableListOf())
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                showSelectedUser(data)
            }
        })
        with(binding){
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUsers.setHasFixedSize(true)
        }
    }

    private fun handleSuccess(listUsers: List<UserEntity>) {
        binding.rvUsers.adapter?.let { adapter ->
            if (adapter is ListUserAdapter){
                adapter.updateList(listUsers)
            }
        }

    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showSelectedUser(user: UserEntity) {
        val moveWithDataIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }
//    private fun showSelectedUser(user: UserDetailEntity) {
//        val moveWithDataIntent = Intent(this@FavoriteActivity, UserAddUpdateActivity::class.java)
//        moveWithDataIntent.putExtra(UserAddUpdateActivity.EXTRA_USER, user)
//        startActivity(moveWithDataIntent)
//    }

//    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        _activityFavoriteBinding = null
//    }
}
