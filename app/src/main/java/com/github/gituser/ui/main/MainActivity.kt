package com.github.gituser.ui.main

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.core.domain.user.model.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityMainBinding
import com.github.gituser.ui.common.showToast
import com.github.gituser.ui.main.detail.DetailActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
//    private var splitInstallManager: SplitInstallManager? = null
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setupRecyclerView()
        observe()

        binding!!.fabAdd.setOnClickListener { view ->
            if (view.id == R.id.fab_add) {
                moveToFavoriteActivity()
            }
        }
    }

    private fun moveToFavoriteActivity() {
        val uri = Uri.parse("github://favorite")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun observe() {
        mainViewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { state ->
            handleStateChanges(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleStateChanges(state: MainActivityState) {
        when (state) {
            is MainActivityState.Init -> Unit
            is MainActivityState.IsSuccess -> handleSuccess(state.userEntity)
            is MainActivityState.IsLoading -> handleLoading(state.isLoading)
            is MainActivityState.IsError -> handleError(state.message)
        }

    }

    private fun handleError(message: String) {
        return showToast(message)
    }

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

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getUsersByQuery(query)
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


        val switchTheme = menu.findItem(R.id.app_bar_switch)
        switchTheme.setActionView(R.layout.switch_item)
        val mySwitch = switchTheme.actionView.findViewById<SwitchMaterial>(R.id.switch_theme)

        mainViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                mySwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                mySwitch.isChecked = false
            }

        }
        mySwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
        return true
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> return true
        }
    }

    private fun setupRecyclerView() {
        val adapter = ListUserAdapter(mutableListOf())
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
        with(binding!!) {
            rvUser.adapter = adapter
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
        }
    }

    private fun handleSuccess(listUsers: List<User>) {

        binding?.rvUser?.adapter?.let { adapter ->
            if (adapter is ListUserAdapter) {
                adapter.updateList(listUsers)
            }
        }

    }

    private fun handleLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}