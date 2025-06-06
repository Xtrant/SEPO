package com.example.sepo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveSession(profileId: Int, profileName: String) {
        prefs.edit {
            putInt(KEY_PROFILE_ID, profileId)
            putString(KEY_PROFILE_NAME, profileName)
        }
    }

    fun saveConditionSession(condition: String) {
        prefs.edit {
            putString(KEY_PROFILE_CONDITION, condition)
        }
    }

    fun isFirstTimeLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        prefs.edit {
            putBoolean(KEY_FIRST_LAUNCH, isFirstTime)
        }
    }

    fun isFirstAppRun(): Boolean {
        return prefs.getBoolean(KEY_FIRST_APP_RUN, true)
    }

    fun setFirstAppRun(isFirstRun: Boolean) {
        prefs.edit {
            putBoolean(KEY_FIRST_APP_RUN, isFirstRun)
        }
    }

    fun getProfileId(): Int = prefs.getInt(KEY_PROFILE_ID, -1)
    fun getProfileName(): String? = prefs.getString(KEY_PROFILE_NAME, "")
    fun getCondition(): String? = prefs.getString(KEY_PROFILE_CONDITION, "")

    fun clearSession() {
        prefs.edit {
            remove(KEY_PROFILE_ID)
            remove(KEY_PROFILE_NAME)
            remove(KEY_PROFILE_CONDITION)
        }
    }




    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_PROFILE_ID = "profile_id"
        private const val KEY_PROFILE_NAME = "profile_name"
        private const val KEY_PROFILE_CONDITION = "profile_condition"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_FIRST_APP_RUN = "first_app_run"
    }
}
