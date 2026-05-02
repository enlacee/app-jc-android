package com.anibalcopitan.miappjc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anibalcopitan.miappjc.ui.theme.MyMonetizableSaaSAppTheme

class MainActivity : ComponentActivity() {

    companion object {

        /**
         * Here is the API SERVICE FOR LOCAL AND PRODUCTION
         */
        const val API_OKEYPAY: String =
            "https://script.google.com/macros/s/AKfycbyKZ4dObth3j7tckVHrHWVlSWeS6jLeDcZ5PJ9Ksxq8DSfyOaDTOm7bdMwIq59qb3dH/exec"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMonetizableSaaSAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyMonetizableSaaSAppTheme {
//        Greeting("Android")
        LoginScreen()
    }
}