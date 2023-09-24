package com.github.gituser.ui.main.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.R
import com.github.gituser.databinding.ActivityDetailBinding
import com.github.gituser.domain.user.entity.UserDetailEntity
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.ui.common.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private var userDetailEntity: UserDetailEntity? = null
//    private var userDetailEntityFavorite: UserDetailEntity? = null
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bindViews()
        observe()
    }

    private fun observe() {
        detailViewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleStateChanges(state)
            }.launchIn(lifecycleScope)
//        detailViewModel.stateUserFavorite.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach { state ->
//                handleFavoriteStateChanges(state)
//            }.launchIn(lifecycleScope)

    }

//    private fun handleFavoriteStateChanges(state: DetailActivityState) {
//        return when (state) {
//            is DetailActivityState.Init -> Unit
//            is DetailActivityState.IsLoading -> handleLoading(state.isLoading)
//            is DetailActivityState.IsSuccess -> handleFavoriteSuccess(state.userDetailEntity)
//            is DetailActivityState.IsError -> handleError(state.message)
//        }
//    }

//    private fun handleFavoriteSuccess(userDetailEntity: UserDetailEntity?) {
//        this.userDetailEntityFavorite = userDetailEntity
//        updateFavoriteButtonListener()
//    }

//    private fun updateFavoriteButtonListener() {
//        Log.d(TAG, "updateFavoriteButtonListener: BUTTON FAVORITE IS SETUP")
//
//
//        binding.btnFavorite.setImageResource(if (this.userDetailEntityFavorite != null) R.drawable.ic_fav else R.drawable.ic_fav_filled)
//
//
//        binding.btnFavorite.setOnClickListener {
//            if (this.userDetailEntityFavorite == null && this.userDetailEntity != null) {
//                detailViewModel.insertUserDetail(this.userDetailEntity!!)
//                showToast("${this.userDetailEntity?.username} has been added to favorite")
//                binding.btnFavorite.setImageResource(R.drawable.ic_fav)
//
//            } else if (this.userDetailEntityFavorite != null) {
//                detailViewModel.deleteUserDetail(this.userDetailEntityFavorite!!)
//                showToast("${this.userDetailEntityFavorite?.username} has removed from favorite")
//                binding.btnFavorite.setImageResource(R.drawable.ic_fav_filled)
//
//            }
//        }
//    }

    private fun handleStateChanges(state: DetailActivityState) {
        return when (state) {
            is DetailActivityState.Init -> Unit
            is DetailActivityState.IsLoading -> handleLoading(state.isLoading)
            is DetailActivityState.IsSuccess -> handleSuccess(state.userDetailEntity!!)
            is DetailActivityState.IsError -> handleError(state.message)
        }
    }

    private fun handleSuccess(userDetailEntity: UserDetailEntity) {
        this.userDetailEntity = userDetailEntity
        handleLoading(false)
//        updateFavoriteButtonListener()
        Glide.with(this)
            .load(userDetailEntity.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        if (userDetailEntity.name != null) {
            binding.tvItemName.text = userDetailEntity.name.toString()
        } else {
            binding.tvItemName.text = userDetailEntity.username
        }
        binding.apply {
            tvItemUsername.text = userDetailEntity.username
            include.tvDataCompany.text = userDetailEntity.company
            include.tvDataLocation.text = userDetailEntity.location
            include.tvDataRepository.text = userDetailEntity.repository.toString()
            include.tvDataFollower.text = userDetailEntity.followers.toString()
            include.tvDataFollowing.text = userDetailEntity.following.toString()
            btnFavorite.setImageResource(if (userDetailEntity.isFavorite!!)  R.drawable.ic_fav else R.drawable.ic_fav_filled)
            btnFavorite.setOnClickListener {
                if(userDetailEntity.isFavorite!!){
                    detailViewModel.deleteUserDetail(userDetailEntity)
                    showToast("${userDetailEntity.username} has been remove from favorite")
                } else {
                    detailViewModel.insertUserDetail(userDetailEntity)
                    showToast("${userDetailEntity.username} has been added to favorite")
                }
            }
        }
    }

    private fun handleError(message: String) {
        return showToast(message)

    }

    private fun bindViews() {
        val user = intent.getParcelableExtra<UserEntity>(EXTRA_USER) as UserEntity
        user.username?.let { username ->
            detailViewModel.apply {
                getUserDetail(username)
//                getAllUser(username)
            }
        }
        binding.btnShare.setOnClickListener(this@DetailActivity)
//        detailViewModel.users.observe(this, {
//            it.getContentIfNotHandled()?.let { data ->
//                detailUser = data
//                setDataUser(data)
//            }
//        })
//        detailViewModel.isLoading.observe(this, {
//            it.getContentIfNotHandled()?.let { status ->
//                showLoading(status)
//            }
//        })
//

        // Bind viewpager
        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity, user.username ?: "")
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.tabTextColors = resources.getColorStateList(R.color.white)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_share) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, """
                    Name: ${userDetailEntity?.name}
                    Username: ${userDetailEntity?.username}
                    Company: ${userDetailEntity?.company}
                    Location: ${userDetailEntity?.location}
                    Repository: ${userDetailEntity?.repository}
                    Follower: ${userDetailEntity?.followers}
                    Following: ${userDetailEntity?.following}
                """.trimIndent()
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share user to.."))
        }
    }

    private fun handleLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "DETAIL_ACTIVITY_TAG"
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}