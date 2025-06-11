package com.studio.ringtonesmartkit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.ringtonesmartkit.api.RingtoneHelper
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import com.studio.ringtonesmartkit.ui.theme.RingtoneSmartKitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RingtoneSmartKitTheme {
                fun tryApplyContactRingtone() {
                    RingtoneHelper.setContactRingtone(
                        source = RingtoneSource.FromAssets(filePath = "me/Best Ringtone.mp3"),
                        contact = ContactIdentifier.Interactive,
                        onSuccess = {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "تم التعيين بنجاح",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        onError = {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    it.message ?: "خطأ غير معروف",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(onClick = {
                            tryApplyContactRingtone()
                        }) {
                            Text("اختيار مقطع صوتي")
                        }
                    }
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
    RingtoneSmartKitTheme {
        Greeting("Android")
    }
}