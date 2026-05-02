package com.anibalcopitan.miappjc

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anibalcopitan.miappjc.data.appconfig.AppConfigManager
import com.anibalcopitan.miappjc.data.phonenumberregistration.SharedPreferencesManager

@Composable
fun CrashTestButton() {
//    val context = LocalContext.current
//
//    Button(onClick = {
//        FirebaseCrashlytics.getInstance().log("Crash test button clicked by anibal")
//        FirebaseCrashlytics.getInstance().log("message")
//        throw RuntimeException("Test Crash from Jetpack Compose by anibal")
//    }) {
//        Text("Test Crash")
//    }
}

@Composable
fun HeaderTextComponent(modifier: Modifier = Modifier) {

    Text(text = stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold, fontSize = 28.sp)
    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "Listo para tu modelo de suscripción",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = "Si eres nuevo, 3 días de versión full",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.error
    )

//    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)
}


// Powered by Anibal Copitan | Soporte: okeypay.anibalcopitan.com o WhatsApp.
@Composable
fun FooterText(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text(text = "okeypay.anibalcopitan.com", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://okeypay.anibalcopitan.com"))
                context.startActivity(intent)
            }
        )
    }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
//        Text(text = "Soporte web",
//            fontSize = 10.sp,
//            color = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.clickable {
//                // Crea el Intent para abrir el navegador
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://okeypay.anibalcopitan.com"))
//                context.startActivity(intent) // Lanza el Intent
//            }
//        )
//        Text(text = " | ", fontSize = 10.sp)
        Text(text = "¿Necesitas ayuda?", fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Crea el Intent para abrir el navegador
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+51970142637"))
                context.startActivity(intent) // Lanza el Intent
            })
        Text(text = " | ", fontSize = 10.sp)

        /*
        val versionName = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "N/A"
        }
        Text(text = "Versión: $versionName", fontSize = 10.sp)
        */

        // ✅ Forma moderna para obtener la versión
        val isInPreview = LocalInspectionMode.current

        val versionName = if (isInPreview) {
            "N/A"
        } else {
            try {
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }
                packageInfo.versionName ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }

        Text(text = "Versión: $versionName", fontSize = 10.sp)
    }

}

@Composable
fun FooterButtonUnlockFullVersion(onClick: () -> Unit) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) } // Estado para controlar la visibilidad del diálogo
    var showSecondDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(0) } // 0 por defecto

    // VALIDACION DATA API
    var sharedPreferencesManager = SharedPreferencesManager(context)
    var subscriptionPlan = sharedPreferencesManager.getString(SharedPreferencesManager.KEY_SUBSCRIPTION_PLAN, "")

    val appConfigManager = AppConfigManager(context)
    val appConfig = appConfigManager.getAppConfig()
    Log.d("appConfig", "Config cargada: $appConfig")
    Log.d("subscriptionPlan", "subscriptionPlan: $subscriptionPlan")

    if (subscriptionPlan != "1") {
        Button(
            //        onClick = { onClick() },
            onClick = { showDialog = true }, // Al hacer clic, mostramos el diálogo
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Alinea el ícono y el texto verticalmente
            ) {
                Icon(
                    imageVector = Icons.Default.Lock, // Ícono de candado del sistema
                    contentDescription = "Candado",
                    modifier = Modifier.size(20.dp), // Tamaño del ícono
                    tint = Color.White // Color del ícono
                )
                Spacer(modifier = Modifier.padding(horizontal = 1.dp)) // Espacio entre el ícono y el texto
                Text(
                    text = "UPGRADE TO THE FULL VERSION",
                    style = TextStyle(fontSize = 14.sp) // Reducción del tamaño del texto
                )
            }
        }
    }

    /**
     * Mostrar el AlertDialog si showDialog es verdadero
     */
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Fondo oscuro con transparencia
        ) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Actualiza a versión full") }, // Nuevo título
                text = {
                    Column {
                        Text(text = "- Elimina publicidad")
                        Text(text = "- Envia SMS a 2 contactos")
                        Text(text = "- Genera reporte de Excel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Acción cuando se confirma (Continuar)
//                            onClick()
                            showDialog = false
                            showSecondDialog = true // Muestra el segundo AlertDialog
                        }
                    ) {
                        Text("Continuar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            // Acción cuando se cancela
                            showDialog = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                },
                shape = RectangleShape, // Borde rectángulo sin redondear
                containerColor = MaterialTheme.colorScheme.surface, // Fondo del contenido
//                contentColor = Color.White // Color de los textos
            )
        }
    }

    /**
     * Segundo AlertDialog con opciones de RadioButton
     */
    if (showSecondDialog) {
        AlertDialog(
            onDismissRequest = { showSecondDialog = false },
            title = { Text(text = "Actualiza a versión completa") },
            text = {
                Column {
                    // Primer RadioButton (auto seleccionado)
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Centra verticalmente
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedOption == 0,
                            onClick = { selectedOption = 0 }
                        )
                        Text(text = "Suscripción Mensual")
                    }

                    // Segundo RadioButton
//                    Row {
//                        RadioButton(
//                            selected = selectedOption == 1,
//                            onClick = { selectedOption = 1 }
//                        )
//                        Text(text = "Opción 2")
//                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        /**
                         * Aquí se abrirá la página web con la opción seleccionada
                         */
                        /**
                         * Aquí se abrirá la página web con la opción seleccionada
                         */
                        /**
                         * Aquí se abrirá la página web con la opción seleccionada
                         */
                        /**
                         * Aquí se abrirá la página web con la opción seleccionada
                         */

//                        val url = "$url?selectedOption=$selectedOption"
                        val url = "https://wa.me/51970142637?text=Me%20gustaria%20adquirir%20el%20Plan%20Mensual.%20Adjunto%20mi%20comprobante%20de%20pago"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        // Aquí puedes incluir el valor seleccionado en los parámetros de la URL o enviarlo como extra
                        context.startActivity(intent)

                        showSecondDialog = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSecondDialog = false }
                ) {
                    Text("Cancelar")
                }
            },
            shape = RectangleShape, // Borde rectángulo
            containerColor = MaterialTheme.colorScheme.surface, // Fondo del contenido
        )
    }

}

@Composable
fun CoinCentered() {

    Box(
        modifier = Modifier.fillMaxWidth(), // solo ancho
        contentAlignment = Alignment.Center // centra horizontal
    ) {
        Text(
            text = "Exclusive Content!",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Box(
        modifier = Modifier.fillMaxWidth(), // solo ancho
        contentAlignment = Alignment.Center // centra horizontal
    ) {
        Image(
            painter = painterResource(id = R.drawable.bitcoin),
            contentDescription = "Bitcoin",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FooterButtonUnlockFullVersionPreview() {
    FooterButtonUnlockFullVersion(onClick = { /* Acción de prueba */ })
}