package com.example.sepo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_PROFILE_ID = "profile_id"
        private const val KEY_PROFILE_NAME = "profile_name"
    }

    // Simpan data
    fun saveSession(profileId: Int, profileName: String) {
        with(prefs.edit()) {

            putInt(KEY_PROFILE_ID, profileId)
            putString(KEY_PROFILE_NAME, profileName)
            apply()
        }
    }

    // Ambil data
    fun getProfileId(): Int = prefs.getInt(KEY_PROFILE_ID, -1)
    fun getProfileName(): String? = prefs.getString(KEY_PROFILE_NAME, null)


    // Hapus session
    fun clearSession() {
        prefs.edit() { clear() }
    }
}
