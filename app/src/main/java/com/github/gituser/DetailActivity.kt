package com.github.gituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: Users
    private lateinit var detailUser: GithubUser
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShare.setOnClickListener(this)
        user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users
//        setDataUser()

        user.username?.let { detailViewModel.getUser(it) }
        detailViewModel.users.observe(this, {
            detailUser = it
            setDataUser(it)
        })
        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setDataUser(data: GithubUser) {
        Glide.with(this)
            .load(data.avatarUrl)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        binding.apply {
            tvItemName.text = data.name?.toString()
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