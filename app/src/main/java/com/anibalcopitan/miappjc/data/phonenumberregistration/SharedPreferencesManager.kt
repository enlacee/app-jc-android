package com.anibalcopitan.miappjc.data.phonenumberregistration

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    companion object {
        public const val KEY_MY_PREFS = "MyPrefs-SharedPreferencesManager-PhoneNumber"
        private const val KEY_PHONE_NUMBER_1 = "phoneNumber1"
        private const val KEY_PHONE_NUMBER_2 = "phoneNumber2"
        private const val KEY_FLAG = "flag"

        // v2
        /**
         * Variables from googleSheet
         */
        const val KEY_ID = "id"
        const val KEY_USERNAME = "username"
        const val KEY_PASSWORD = "password"
        const val KEY_NAME = "name"
        const val KEY_SUBSCRIPTION_START_DATE = "subscription_start_date" // date example: 2024-12-17
        const val KEY_SUBSCRIPTION_DURATION_DAYS = "subscription_duration_days" // from 0 to 30
        const val KEY_SUBSCRIPTION_PLAN = "subscription_plan" // Enable= 1, Disabled= 0
        const val KEY_GOOGLE_SHEET_URL = "google_sheet_url"

        /**
         * Variables internal app
         */
        const val KEY_COUNTER = "counter"
    }
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(KEY_MY_PREFS, Context.MODE_PRIVATE)

    fun saveFormData(phoneNumber1: String, phoneNumber2: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PHONE_NUMBER_1, phoneNumber1)
        editor.putString(KEY_PHONE_NUMBER_2, phoneNumber2)

        editor.apply()
    }

    fun getFormData(): FormModel {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""
        return FormModel(phoneNumber1, phoneNumber2)
    }

    fun getPhoneNumbers(): Array<String> {
        val phoneNumber1 = sharedPreferences.getString(KEY_PHONE_NUMBER_1, "") ?: ""
        val phoneNumber2 = sharedPreferences.getString(KEY_PHONE_NUMBER_2, "") ?: ""

        // Crear y retornar un array con los números de teléfono obtenidos
        return arrayOf(phoneNumber1, phoneNumber2)
    }

    /**
     * save data flag
     */
    fun saveFormDataFlag(flag:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_FLAG, flag)

        editor.apply()
    }
    fun getFormDataFlag(): FormModelFlag {
        val flag = sharedPreferences.getBoolean(KEY_FLAG, true) ?: true
        return FormModelFlag(flag)
    }

    data class FormModel(val phoneNumber1: String, val phoneNumber2: String)
    data class FormModelFlag(val flag: Boolean)


    //    extra functionality
    fun getString(keyString: String, valueDefault: String): String {
        return sharedPreferences.getString(keyString, valueDefault) ?: ""
    }

    fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /*
    * Counter
    * */
    fun getCounter(): Int {
        return sharedPreferences.getInt(KEY_COUNTER, 0)
    }

    fun setCounter(counter: Int) {
        sharedPreferences.edit().putInt(KEY_COUNTER, counter).apply()
    }
    fun registerCounterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterCounterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}

