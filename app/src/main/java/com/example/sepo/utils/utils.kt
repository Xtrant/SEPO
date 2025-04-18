package com.example.sepo.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.sepo.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showLoading(isLoading: Boolean, item: ProgressBar) {
    item.visibility = if (isLoading) View.VISIBLE else View.GONE
}