package com.github.gituser.ui.insert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.database.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityUserAddUpdateBinding
import com.github.gituser.helper.ViewModelFactory
import com.github.gituser.ui.main.FollowActivity

class UserAddUpdateActivity : AppCompatActivity() {
    private var user: User? = null
    private lateinit var userAddUpdateViewModel: UserAddUpdateViewModel
    private var _activityUserAddUpdateBinding: ActivityUserAddUpdateBinding? = null
    private val binding get() = _activityUserAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityUserAddUpdateBinding = ActivityUserAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAddUpdateViewModel = obtainViewModel(this@UserAddUpdateActivity)

        user = intent.getParcelableExtra(EXTRA_USER)
        user?.let { user ->
            Glide.with(this)
                .load(user.avatar)
                .apply(RequestOptions().override(120, 120))
                .into(binding?.profileUser!!)
            binding?.apply {
                tvItemName?.setText(user.name)
                tvItemUsername?.setText(user.username)
                include.tvDataCompany.text = user.company
                include.tvDataLocation.text = user.location
                include.tvDataRepository.text = user.repository.toString()
                include.tvDataFollower.text = user.followers.toString()
                include.tvDataFollowing.text = user.following.toString()
            }
        }

        supportActionBar?.title = user?.username

        binding?.btnShare?.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT, """
                Name: ${user?.name}
                Username: ${user?.username}
                Company: ${user?.company}
                Location: ${user?.location}
                Repository: ${user?.repository}
                Follower: ${user?.followers}
                Following: ${user?.following}
            """.trimIndent())
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share user to.."))
        }

        binding?.btnFollow?.setOnClickListener{
            val followIntent: Intent = Intent(this, FollowActivity::class.java).apply {
                putExtra(FollowActivity.USERNAME, user?.username)
            }
            startActivity(followIntent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val dialogTitle: String = getString(R.string.delete)
        val dialogMessage: String = getString(R.string.message_delete)
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                userAddUpdateViewModel.delete(user as User)
                showToast(getString(R.string.deleted))
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityUserAddUpdateBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserAddUpdateViewModel::class.java)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}