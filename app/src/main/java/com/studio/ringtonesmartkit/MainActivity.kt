/*
 * Copyright 2025 Amjd Alhashede
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.studio.ringtonesmartkit

import android.os.Bundle
import android.os.Environment
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
import androidx.core.net.toUri
import com.studio.ringtonesmartkit.ui.theme.RingtoneSmartKitTheme
import io.domain.model.ContactIdentifier
import io.domain.model.RingtoneSource
import io.github.ringtonesmartkit.api.RingtoneHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RingtoneSmartKitTheme {
                fun tryApplyRingtone() {
                    RingtoneHelper.setSystemRingtone(source = RingtoneSource.FromAssets(filePath = "ringtone_name.mp3"))
                        .onSuccess {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity, "تم التعيين بنجاح", Toast.LENGTH_LONG
                                ).show()
                            }
                        }.onFailure { error ->
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    error.message ?: "خطأ غير معروف",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }

                fun tryApplyContactRingtone() {
                    RingtoneHelper.setContactRingtone(
                        source = RingtoneSource.FromAssets(filePath = "ringtones/Guitar.mp3"),
                        contact = ContactIdentifier.ById(id = 56177)
                    ).onSuccess { contactInfo ->
                        println("==================================== $contactInfo =================== ")
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity, "${contactInfo?.displayName}", Toast.LENGTH_LONG
                            ).show()
                        }
                    }.onFailure { error ->
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                error.message ?: "خطأ غير معروف",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
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
                            tryApplyRingtone()
                        }) {
                            Text("اختيار مقطع صوتي")
                        }
                        Button(onClick = {
                            tryApplyContactRingtone()
                        }) {
                            Text("اختيار مقطع d")
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
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RingtoneSmartKitTheme {
        Greeting("Android")
    }
}