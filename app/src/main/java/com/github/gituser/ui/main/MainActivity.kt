package com.github.gituser.ui.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.gituser.R
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.ui.common.SettingPreferences
import com.github.gituser.ui.common.showToast
import com.github.gituser.ui.main.detail.DetailActivity
import com.github.gituser.ui.main.favorite.FavoriteActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.USER_PREFERENCE)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
//    private val darkViewModel by viewModels<DarkViewModel>()
//    @Inject lateinit var pref: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        observe()

//        mainViewModel.users.observe(this, {
//            setUsersData(it)
//        })
//        mainViewModel.isLoading.observe(this, {
//            showLoading(it)
//        })

        binding.fabAdd.setOnClickListener { view ->
            if (view.id == R.id.fab_add) {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun observe() {
        mainViewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach {
            state -> handleStateChanges(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleStateChanges(state: MainActivityState) {
        when(state){
            is MainActivityState.Init -> Unit
            is MainActivityState.IsSuccess -> handleSuccess(state.userEntity)
            is MainActivityState.IsLoading -> handleLoading(state.isLoading)
            is MainActivityState.IsError -> handleError(state.message)
        }

    }

    private fun handleError(message: String) {
        return showToast(message)
    }

    private fun showSelectedUser(user: UserEntity) {
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
//                mainViewModel.users.observe(this@MainActivity, {
//                    setUsersData(it)
//                })
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

//        val pref = SettingPreferences.getInstance(dataStore)
//        val darkViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
//            DarkViewModel::class.java
//        )
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                    mySwitch.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                    mySwitch.isChecked = false
                }

            })
        mySwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> return true
        }
    }

    private fun setupRecyclerView(){
        val adapter = ListUserAdapter(mutableListOf())
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                showSelectedUser(data)
            }
        })
        with(binding){
            rvUser.adapter = adapter
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
        }
    }

    private fun handleSuccess(listUsers: List<UserEntity>) {
//        val listUsers = ArrayList<UserEntity>()
//        for (item in listItems) {
//            val user = UserEntity(
//                item.login,
//                item.avatarUrl
//            )
//            listUsers.add(user)
//        }

        binding.rvUser.adapter?.let { adapter ->
            if (adapter is ListUserAdapter){
                adapter.updateList(listUsers)
            }
        }

    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}