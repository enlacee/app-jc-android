package com.anibalcopitan.miappjc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.anibalcopitan.miappjc.data.appconfig.AppConfigClass
import com.anibalcopitan.miappjc.data.phonenumberregistration.SharedPreferencesManager
import com.anibalcopitan.miappjc.ui.theme.MyMonetizableSaaSAppTheme
import com.anibalcopitan.miappjc.ui.theme.Shapes
import org.json.JSONObject



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenScreenPreview() {
    MyMonetizableSaaSAppTheme{
        LoginScreen()
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    Log.i("BOTON-REGISTRAR", "=== START LoginScreen === " )
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    val openDialog = remember { mutableStateOf(false) }
    var credentials by remember { mutableStateOf(
        Credentials(
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, ""),
            sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "")
        )
    ) }
    val focusManager = LocalFocusManager.current
    var isProcessing by remember { mutableStateOf(false) }

    Log.i("BOTON-REGISTRAR", "[usernamexxx ] " + sharedPreferencesManager.getString(SharedPreferencesManager.KEY_USERNAME, "") )
    Log.i("BOTON-REGISTRAR", "[passxxx ] " + sharedPreferencesManager.getString(SharedPreferencesManager.KEY_PASSWORD, "") )

    Column(modifier = Modifier.padding(16.dp)) {
        HeaderText()

        OutlinedTextField(
            value = credentials.login,
            onValueChange = { data -> credentials = credentials.copy(login = data) },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )
        //
        Spacer(modifier = Modifier.height(4.dp))
        //PasswordTextField()
        OutlinedTextField(
            value = credentials.pwd,
            onValueChange = { data -> credentials = credentials.copy(pwd = data) },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isProcessing) {
                CircularProgressIndicator()
            } else {
                ButtonLogin() {

                    if (formValidation(credentials, context)) {
                        isProcessing = true
                        val params: MutableMap<String?, String?> = HashMap()
                        params["op"] = "login"
                        params["email"] = credentials.login
                        params["password"] = credentials.pwd
                        val parameters = (params as Map<*, *>?)?.let { JSONObject(it) }
                        val url = MainActivity.API_OKEYPAY
                        val request = object : JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            parameters,
                            { theResponse ->

                                Log.i("BOTON-REGISTRAR", "response Login OK vollley")

                                if (theResponse.has("message")) {
                                    Toast.makeText(context, theResponse.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                                if (
                                    theResponse.has("status") &&
                                    theResponse.getString("status").equals("ok") &&
                                    theResponse.has("data")
                                ) {

                                    val dataObject = theResponse.getJSONObject("data")

                                    val appConfigClass = AppConfigClass.getInstance(context)
                                    val appConfig = appConfigClass.loadAppConfig()

                                    appConfig.username = credentials.login
                                    appConfig.password = credentials.pwd
                                    appConfig.id = dataObject.optString("id", "")
                                    appConfig.name = dataObject.optString("name", "")
                                    appConfig.subscriptionStartDate = dataObject.optString("subscriptionStartDate", "")
                                    appConfig.subscriptionDurationDays = dataObject.optString("subscriptionDurationDays", "")
                                    appConfig.subscriptionPlan = dataObject.optString("subscriptionPlan", "")
                                    appConfig.googleSheetUrl = dataObject.optString("googleSheetUrl", "")

                                    appConfigClass.saveAppConfig(appConfig)

                                    SubscriptionWorker.autoValidationOfSubscriptionPlan(context)
                                    openDashboardActivity(context)
                                }

                                isProcessing = false
                            },
                            { error ->
                                Log.i("BOTON-REGISTRAR", "Error: $error")
                                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                                isProcessing = false
                            }
                        ) {

                            // 🔥 Esto elimina el charset
                            override fun getBodyContentType(): String {
                                return "application/json"
                            }

                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-Type"] = "application/json"
                                return headers
                            }
                        }
                        request.retryPolicy = DefaultRetryPolicy(
                            10000, // 10 segundos espera
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                        MySingleton.getInstance(context).addToRequestQueue(request)

                        // 00. authentication login [consulta api]

                        // 01. Save Data (prefs) (Si falla igual guardar las credenciales ingresadas)
                        val appConfigClass = AppConfigClass.getInstance(context)
                        val appConfig = appConfigClass.loadAppConfig()
                        appConfig.username = credentials.login
                        appConfig.password = credentials.pwd
                        appConfigClass.saveAppConfig(appConfig)
                        Log.i("BOTON-REGISTRAR", "=== ButtonLogin === ")
                        Log.i("BOTON-REGISTRAR", "CONFIG username " + appConfig.username)
                        Log.i("BOTON-REGISTRAR", "CONFIG password " + appConfig.password)

                        // 02. abrir activity Dashboard
//                        openDashboardActivity(context)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(44.dp))
        ButtonToRegister { openDialog.value = true }
        FooterText()
        FooterButtonUnlockFullVersion({})
    }

    // Open Modal
    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 0.dp)
            ){
                /*
                * Open Forn Register
                * */
                RegisterScreen(
                    context,
                    object : DialogCallback {
                        override fun closeDialog() {
                            openDialog.value = false
                            openDashboardActivity(context) // open dashboard FAST
                        }
                    },
                    onSaveClicked = { theUsername, thePassword ->
                        credentials.login = theUsername
                        credentials.pwd = thePassword
                        //Log.d("Formulario1", "Nombre: $nombre, Apellido: $apellido") // Imprimir en la consola
                    }
                )
            }
        }
    }
}

/*
* Open DashboardActivity
*
* return void
* */
fun openDashboardActivity(context: Context){
    context.startActivity(Intent(context, DashboardActivity::class.java)) // abre una nueva activity
    (context as Activity).finish() // cierra el actual activity *login*
}

data class Credentials(
    var login: String = "",
    var pwd: String = "",
) {
    fun isEmpty(): Boolean {
        return login.isEmpty() && pwd.isEmpty()
    }

    fun isEmailValid(): Boolean {
        val email: String = login
        val regexPattern = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        return regexPattern.matches(email)
    }

    fun isPasswordValid(): Boolean {
        return pwd.length >= 6
    }
}

/*
* validation of form
* */
fun formValidation(credentials: Credentials, context: Context): Boolean {
    if (credentials.isEmpty()) {
        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!credentials.isEmailValid()) {
        Toast.makeText(context, "El correo no es válido", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!credentials.isPasswordValid()) {
        Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

@Composable
private fun HeaderText() {
    HeaderTextComponent()
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = "Log in to continue", fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
private fun ButtonLogin(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 1.dp),
        shape = Shapes.large
    ) {
        Text("Log In")
    }
}

@Composable
private fun ButtonToRegister(onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("¿Do you have account? ")
        Text("Register ",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}