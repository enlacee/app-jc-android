package com.anibalcopitan.miappjc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anibalcopitan.miappjc.data.appconfig.AppConfigManager
import com.anibalcopitan.miappjc.data.phonenumberregistration.SharedPreferencesManager

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenScreenPreview() {
//    OkeyPay2Theme {
    DashboardScreen()
//    }
}

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)

    // get data preferences
    /**
     * DEBUG to remove code
     * - test if pass data sharepreference
     */
    val appConfigManager = AppConfigManager(context)
    val appConfig = appConfigManager.getAppConfig()
    val subscriptionPlan = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")
    Log.d("SubscriptionWorker", "Config cargada 222: $appConfig")
    Log.d("SubscriptionWorker", "subscriptionPlan 222: $subscriptionPlan")
    /**
     * end code to remove up
     */

    Column(modifier = Modifier.padding(16.dp)) {
        HeaderTextComponent()
        Spacer(modifier = Modifier.height(8.dp))
        inputHeaderText(sharedPreferencesManager)
        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Control absolute by subscriptionPlan = 1
         *
         * Need to pass workers to check every hours the suscription by status or days
         * The result is just 1 or 0
         */
        if (subscriptionPlan == "1") {
            CoinCentered()
        }

//        if (BuildConfig.ENABLE_INPUT) {
//            CoinCentered()
//        }
        FooterText()
        FooterButtonUnlockFullVersion({})
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputHeaderText(sharedPreferencesManager: SharedPreferencesManager) {
    Text(
        text = "Correo: ${sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "")}",
        fontSize = 16.sp,
//        fontWeight = FontWeight.Bold
    )
}

fun copyToClipboard(text: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("URL", text)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, "URL copiada al portapapeles", Toast.LENGTH_SHORT).show()
}
