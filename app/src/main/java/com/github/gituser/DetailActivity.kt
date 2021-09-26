package com.github.gituser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: User

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
        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        setDataUser()
    }

    private fun setDataUser() {
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().override(120, 120))
            .into(binding.profile)
        binding.tvItemName.text = user.name
        binding.tvItemUsername.text = user.username
        binding.include.tvDataCompany.text = user.company
        binding.include.tvDataLocation.text = user.location
        binding.include.tvDataRepository.text = user.repository.toString()
        binding.include.tvDataFollower.text = user.follower.toString()
        binding.include.tvDataFollowing.text = user.following.toString()

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
}