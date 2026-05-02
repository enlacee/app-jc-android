package com.anibalcopitan.miappjc

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.anibalcopitan.miappjc.ui.theme.MyMonetizableSaaSAppTheme

class DashboardActivity : ComponentActivity() {

//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * SubscriptionWorker
         * Active the worker here! this activity! (each 1 hour)
         */
        scheduleSubscriptionCheck(this)

        val isRunning = isSubscriptionWorkerRunning(this)
        Log.d("WorkerCheck", "¿SubscriptionWorker está corriendo? $isRunning")

        // Ejecutar el Worker una sola vez para pruebas & al iniciar el dashboard se ejecuta
        startOneTimeWorker()

        setContent {
            MyMonetizableSaaSAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen() // abriendo funcion NO VISTA PRE PREVIA
//                    DashboardScreenView() // ESTE ES VISTA PRE PREVIA
                }
            }
        }
    }

    /**
     * test
     * ejecutar una sola ves el worker
     */
    private fun startOneTimeWorker() {
        val testWorkRequest = OneTimeWorkRequestBuilder<SubscriptionWorker>().build()
        WorkManager.getInstance(this).enqueue(testWorkRequest)

        Log.d("WorkerTest", "OneTimeWorkRequest encolado")
    }
}

/**
 * task worker: ejecutar cada hora!!
 */
fun scheduleSubscriptionCheck(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<SubscriptionWorker>(1, java.util.concurrent.TimeUnit.HOURS)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SubscriptionWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

/**
 * test: task worker
 */
fun isSubscriptionWorkerRunning(context: Context): Boolean {
    // DEBUG 1
    Log.d("Worker Running", "SubscriptionWorker")
    val workManager = WorkManager.getInstance(context)
    val workInfos = workManager.getWorkInfosForUniqueWork("SubscriptionWorker").get()
    return workInfos.any { it.state == androidx.work.WorkInfo.State.ENQUEUED || it.state == androidx.work.WorkInfo.State.RUNNING }
}



@Composable
@Preview(showBackground = true, showSystemUi = true, apiLevel = 33)
fun DashboardScreenView() {
    DashboardScreen()
}
