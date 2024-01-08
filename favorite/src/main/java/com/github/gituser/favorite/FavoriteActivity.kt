package com.github.gituser.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.core.domain.user.model.User
import com.github.gituser.R
import com.github.gituser.favorite.databinding.ActivityFavoriteBinding
import com.github.gituser.ui.common.showToast
import com.github.gituser.ui.main.ListUserAdapter
import com.github.gituser.ui.main.detail.DetailActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadKoinModules(favoriteViewModelModule)
        supportActionBar?.title = resources.getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        observe()
    }

    private fun observe() {
        favoriteViewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleStateChanges(state)
            }.launchIn(lifecycleScope)
    }

    private fun handleStateChanges(state: FavoriteActivityState) {
        when (state) {
            is FavoriteActivityState.Init -> Unit
            is FavoriteActivityState.IsSuccess -> handleSuccess(state.user)
            is FavoriteActivityState.IsLoading -> handleLoading(state.isLoading)
            is FavoriteActivityState.IsError -> handleError(state.message)
        }

    }

    private fun handleError(message: String) {
        return showToast(message)
    }

    private fun setupRecyclerView() {
        val adapter = ListUserAdapter(mutableListOf())
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
        with(binding) {
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUsers.setHasFixedSize(true)
        }
    }

    private fun handleSuccess(listUsers: List<User>) {
        binding.rvUsers.adapter?.let { adapter ->
            if (adapter is ListUserAdapter) {
                adapter.updateList(listUsers)
            }
        }

    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(user: User) {
        val moveWithDataIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }
}
