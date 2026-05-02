package com.anibalcopitan.miappjc.data.appconfig


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.anibalcopitan.miappjc.data.phonenumberregistration.SharedPreferencesManager

// Clase para representar la configuración
data class AppConfig(
    var id: String,
    var username: String,
    var password: String,
    var name: String,
    var subscriptionStartDate: String,
    var subscriptionDurationDays: String,
    var subscriptionPlan: String,
    var googleSheetUrl: String
)

// Builder para crear la configuración
private class AppConfigBuilder(private val sharedPreferencesManager: SharedPreferencesManager) {

    private var id: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_ID, "")
    private var username: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "")
    private var password: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "")
    private var name: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_NAME, "")
    private var subscriptionStartDate: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, "")
    private var subscriptionDurationDays: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, "")
    private var subscriptionPlan: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
    private var googleSheetUrl: String = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "")

    fun build(): AppConfig {
        return AppConfig(id, username, password, name, subscriptionStartDate, subscriptionDurationDays, subscriptionPlan, googleSheetUrl )
    }
}

class AppConfigClass(private val sharedPreferencesManager: SharedPreferencesManager) {

    private val appConfigBuilder = AppConfigBuilder(sharedPreferencesManager)

    fun saveAppConfig(appConfig: AppConfig) {
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_ID, appConfig.id)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_USERNAME, appConfig.username)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_PASSWORD, appConfig.password)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_NAME, appConfig.name)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, appConfig.subscriptionStartDate)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, appConfig.subscriptionDurationDays)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, appConfig.subscriptionPlan)
        sharedPreferencesManager.saveString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, appConfig.googleSheetUrl)
    }

    fun loadAppConfig(): AppConfig {
        return appConfigBuilder.build()
    }

    companion object {
        private var instance: AppConfigClass? = null

        fun getInstance(context: Context): AppConfigClass {
            if (instance == null) {
                val sharedPreferencesManager = SharedPreferencesManager(context)
                instance = AppConfigClass(sharedPreferencesManager)
            }
            return instance!!
        }
    }

}

/**
 * V2 integration
 * Alternative to get and set data EASY
 */
class AppConfigManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SharedPreferencesManager.KEY_MY_PREFS, Context.MODE_PRIVATE)

    fun getAppConfig(): AppConfig {
        return AppConfig(
            id = sharedPreferences.getString(SharedPreferencesManager.KEY_ID, "") ?: "",
            username = sharedPreferences.getString(SharedPreferencesManager.KEY_USERNAME, "") ?: "",
            password = sharedPreferences.getString(SharedPreferencesManager.KEY_PASSWORD, "") ?: "",
            name = sharedPreferences.getString(SharedPreferencesManager.KEY_NAME, "") ?: "",
            subscriptionStartDate = sharedPreferences.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, "") ?: "",
            subscriptionDurationDays = sharedPreferences.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, "") ?: "",
            subscriptionPlan = sharedPreferences.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "") ?: "",
            googleSheetUrl = sharedPreferences.getString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, "") ?: ""
        )
    }

    fun saveAppConfig(appConfig: AppConfig) {
        Log.d("AppConfigManager", "Guardando configuración: $appConfig")
//        sharedPreferences.edit().apply {
//            putString(SharedPreferencesManager.KEY_ID, appConfig.id)
//            putString(SharedPreferencesManager.KEY_USERNAME, appConfig.username)
//            putString(SharedPreferencesManager.KEY_PASSWORD, appConfig.password)
//            putString(SharedPreferencesManager.KEY_NAME, appConfig.name)
//            putString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, appConfig.subscriptionStartDate)
//            putString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, appConfig.subscriptionDurationDays)
//            putString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, appConfig.subscriptionPlan)
//            putString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, appConfig.googleSheetUrl)
//            apply()  // Guarda los cambios en SharedPreferences
//        }

        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_ID, appConfig.id).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_USERNAME, appConfig.username).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_PASSWORD, appConfig.password).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_NAME, appConfig.name).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_SUBSCRIPTION_START_DATE, appConfig.subscriptionStartDate).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_SUBSCRIPTION_DURATION_DAYS, appConfig.subscriptionDurationDays).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, appConfig.subscriptionPlan).apply()
        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_GOOGLE_SHEET_URL, appConfig.googleSheetUrl).apply()

//        sharedPreferences.edit().putString(SharedPreferencesManager.KEY_ID, appConfig.id).commit()

    }
}