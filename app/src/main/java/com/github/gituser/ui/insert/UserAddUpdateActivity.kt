package com.github.gituser.ui.insert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.Database.User
import com.github.gituser.R
import com.github.gituser.databinding.ActivityUserAddUpdateBinding
import com.github.gituser.helper.ViewModelFactory

//class UserAddUpdateActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_user_add_update)
//    }
//}

class UserAddUpdateActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var isEdit = false
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
        if (user != null) {
            isEdit = true
        } else {
            user = User()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.detail_user)
            btnTitle = getString(R.string.remove_favorite)
            if (user != null) {
                user?.let { user ->
                    Glide.with(this)
                        .load(user.avatar)
                        .apply(RequestOptions().override(120, 120))
                        .into(binding?.profileUser!!)
                    binding?.tvItemName?.setText(user.username)
                    binding?.tvItemUsername?.setText(user.username)
                }
            }
        } else {
            actionBarTitle = getString(R.string.detail_user)
            btnTitle = getString(R.string.remove_favorite)
        }

        supportActionBar?.title = actionBarTitle
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        binding?.btnSubmit?.text = btnTitle
//        binding?.btnSubmit?.setOnClickListener {
//            val title = binding?.edtTitle?.text.toString().trim()
//            val description = binding?.edtDescription?.text.toString().trim()
//            when {
//                title.isEmpty() -> {
//                    binding?.edtTitle?.error = getString(R.string.empty)
//                }
//                description.isEmpty() -> {
//                    binding?.edtDescription?.error = getString(R.string.empty)
//                }
//                else -> {
//                    user.let { user ->
//                        user?.username = title
//                        user?.avatar = description
//                    }
//                    if (isEdit) {
//                        userAddUpdateViewModel.update(user as User)
//                        showToast(getString(R.string.changed))
//                    } else {
////                        note.let { note ->
////                            note?.date = DateHelper.getCurrentDate()
////                        }
//                        userAddUpdateViewModel.insert(user as User)
//                        showToast(getString(R.string.added))
//                    }
//                    finish()
//                }
//            }
//        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    userAddUpdateViewModel.delete(user as User)
                    showToast(getString(R.string.deleted))
                }
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
}