package com.github.gituser.ui.common

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.gituser.R

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton(getString(R.string.yes)){ dialog, _ ->
             dialog.dismiss()
        }
    }.show()
}