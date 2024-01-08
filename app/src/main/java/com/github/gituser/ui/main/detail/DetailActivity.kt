package com.github.gituser.ui.main.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.core.domain.user.model.User
import com.github.core.domain.user.model.UserDetail
import com.github.gituser.R
import com.github.gituser.databinding.ActivityDetailBinding
import com.github.gituser.ui.common.showToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private var userDetail: UserDetail? = null
    private val detailViewModel: DetailViewModel by viewModel()

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
    }

    private fun handleStateChanges(state: DetailActivityState) {
        return when (state) {
            is DetailActivityState.Init -> Unit
            is DetailActivityState.IsLoading -> handleLoading(state.isLoading)
            is DetailActivityState.IsSuccess -> handleSuccess(state.userDetail!!)
            is DetailActivityState.IsError -> handleError(state.message)
        }
    }

    private fun handleSuccess(userDetail: UserDetail) {
        this.userDetail = userDetail
        handleLoading(false)
//        updateFavoriteButtonListener()
        Glide.with(this)
            .load(userDetail.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        if (userDetail.name != null) {
            binding.tvItemName.text = userDetail.name.toString()
        } else {
            binding.tvItemName.text = userDetail.username
        }
        binding.apply {
            tvItemUsername.text = userDetail.username
            include.tvDataCompany.text = userDetail.company
            include.tvDataLocation.text = userDetail.location
            include.tvDataRepository.text = userDetail.repository.toString()
            include.tvDataFollower.text = userDetail.followers.toString()
            include.tvDataFollowing.text = userDetail.following.toString()
            btnFavorite.setImageResource(if (userDetail.isFavorite!!) R.drawable.ic_fav else R.drawable.ic_fav_filled)
            btnFavorite.setOnClickListener {
                if (userDetail.isFavorite!!) {
                    detailViewModel.deleteUserDetail(userDetail)
                    showToast("${userDetail.username} has been remove from favorite")
                } else {
                    detailViewModel.insertUserDetail(userDetail)
                    showToast("${userDetail.username} has been added to favorite")
                }
            }
        }
    }

    private fun handleError(message: String) {
        return showToast(message)
    }

    private fun bindViews() {
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        user.username?.let { username ->
            detailViewModel.apply {
                getUserDetail(username)
            }
        }
        binding.btnShare.setOnClickListener(this@DetailActivity)

        // Bind viewpager
        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity, user.username ?: "")
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabs.tabTextColors = resources.getColorStateList(R.color.white, null)
        } else {
            tabs.tabTextColors = ContextCompat.getColorStateList(this, R.color.white)
        }


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
                    Name: ${userDetail?.name}
                    Username: ${userDetail?.username}
                    Company: ${userDetail?.company}
                    Location: ${userDetail?.location}
                    Repository: ${userDetail?.repository}
                    Follower: ${userDetail?.followers}
                    Following: ${userDetail?.following}
                """.trimIndent()
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share user to.."))
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}