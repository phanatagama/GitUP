package com.github.gituser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
<<<<<<< HEAD:app/src/main/java/com/github/gituser/ui/main/DetailActivity.kt
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
=======
>>>>>>> 2d80f1f (Revert "Add Detail Activity"):app/src/main/java/com/github/gituser/DetailActivity.kt
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.database.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityDetailBinding
import com.github.gituser.helper.ViewModelFactory
import com.github.gituser.ui.insert.UserAddUpdateViewModel


class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
<<<<<<< HEAD:app/src/main/java/com/github/gituser/ui/main/DetailActivity.kt
    private lateinit var user: Users
    private var userF: User = User()
    private lateinit var detailUser: GithubUser
    private lateinit var userAddUpdateViewModel: UserAddUpdateViewModel
    private val detailViewModel by viewModels<DetailViewModel>()
=======
    private lateinit var user: User
>>>>>>> 2d80f1f (Revert "Add Detail Activity"):app/src/main/java/com/github/gituser/DetailActivity.kt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShare.setOnClickListener(this)
<<<<<<< HEAD:app/src/main/java/com/github/gituser/ui/main/DetailActivity.kt
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
                userF.username = detailUser.login
                userF.avatar = detailUser.avatarUrl
                userF.name = detailUser.name.toString()
                userF.company = detailUser.company
                userF.location = detailUser.location
                userF.repository = detailUser.publicRepos
                userF.followers = detailUser.followers
                userF.following = detailUser.following
            }
            userAddUpdateViewModel.insert(userF as User)
            Toast.makeText(this@DetailActivity, "Favorite ${user.username}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserAddUpdateViewModel::class.java)
    }

    private fun setDataUser(data: GithubUser) {
=======
        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        setDataUser()
    }

    private fun setDataUser() {
>>>>>>> 2d80f1f (Revert "Add Detail Activity"):app/src/main/java/com/github/gituser/DetailActivity.kt
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        binding.apply {
<<<<<<< HEAD:app/src/main/java/com/github/gituser/ui/main/DetailActivity.kt
            tvItemName.text = data.name.toString()
            tvItemUsername.text = data.login
            include.tvDataCompany.text = data.company
            include.tvDataLocation.text = data.location
            include.tvDataRepository.text = data.publicRepos.toString()
            include.tvDataFollower.text = data.followers.toString()
            include.tvDataFollowing.text = data.following.toString()
=======
            tvItemName.text = user.name
            tvItemUsername.text = user.username
            include.tvDataCompany.text = user.company
            include.tvDataLocation.text = user.location
            include.tvDataRepository.text = user.repository.toString()
            include.tvDataFollower.text = user.follower.toString()
            include.tvDataFollowing.text = user.following.toString()
>>>>>>> 2d80f1f (Revert "Add Detail Activity"):app/src/main/java/com/github/gituser/DetailActivity.kt
        }
    }

    override fun onClick(p0: View?) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, """
                Name: ${user.name}
                Username: ${user.username}
                Company: ${user.company}
                Location: ${user.location}
                Repository: ${user.repository}
                Follower: ${user.follower}
                Following: ${user.following}
            """.trimIndent())
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share user to.."))
    }
<<<<<<< HEAD:app/src/main/java/com/github/gituser/ui/main/DetailActivity.kt

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
=======
>>>>>>> 2d80f1f (Revert "Add Detail Activity"):app/src/main/java/com/github/gituser/DetailActivity.kt
}