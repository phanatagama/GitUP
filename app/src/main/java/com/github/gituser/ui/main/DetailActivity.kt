package com.github.gituser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.Database.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityDetailBinding
import com.github.gituser.helper.ViewModelFactory
import com.github.gituser.ui.insert.UserAddUpdateViewModel


class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: Users
    private var userF: User = User()
    private lateinit var detailUser: GithubUser
    private lateinit var userAddUpdateViewModel: UserAddUpdateViewModel
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShare.setOnClickListener(this)
        binding.btnFollow.setOnClickListener(View.OnClickListener {
            val followIntent: Intent = Intent(this, FollowActivity::class.java).apply {
                putExtra(FollowActivity.USERNAME, detailUser.login)
            }
            startActivity(followIntent)
        })
        user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users
        user.username?.let { detailViewModel.getUser(it) }
        detailViewModel.users.observe(this, {
            it.getContentIfNotHandled()?.let { data ->
                detailUser = data
                setDataUser(data)
            }
        })
        detailViewModel.isLoading.observe(this, {
            it.getContentIfNotHandled()?.let { status ->
                showLoading(status)
            }
        })

        userAddUpdateViewModel = obtainViewModel(this@DetailActivity)
        binding.btnFav.setOnClickListener( View.OnClickListener {
            userF.let { userF ->
                userF.username = user.username
                userF.avatar = user.avatar
            }
            userAddUpdateViewModel.insert(userF as User)
            Toast.makeText(this@DetailActivity, user.username, Toast.LENGTH_SHORT).show()
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserAddUpdateViewModel::class.java)
    }

    private fun setDataUser(data: GithubUser) {
        Glide.with(this)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        binding.apply {
            tvItemName.text = data.name.toString()
            tvItemUsername.text = data.login
            include.tvDataCompany.text = data.company
            include.tvDataLocation.text = data.location
            include.tvDataRepository.text = data.publicRepos.toString()
            include.tvDataFollower.text = data.followers.toString()
            include.tvDataFollowing.text = data.following.toString()
        }
    }

    override fun onClick(p0: View?) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, """
                Name: ${detailUser.name}
                Username: ${detailUser.login}
                Company: ${detailUser.company}
                Location: ${detailUser.location}
                Repository: ${detailUser.publicRepos}
                Follower: ${detailUser.followers}
                Following: ${detailUser.following}
            """.trimIndent())
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share user to.."))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}