package com.example.mlkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mlkit.ui.theme.MLKitTheme
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitTheme {
                var text by remember { mutableStateOf("") }

                val translator = remember {
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.KOREAN)
                        .build()
                    Translation.getClient(options)
                }
                var enabled by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(Unit) {
                    val conditions = DownloadConditions.Builder()
                        .requireWifi()
                        .build()
                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            enabled = true
                        }
                        .addOnFailureListener { exception ->
                        }
                }
                var textTranslated by remember { mutableStateOf("") }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text("글 입력하기")
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = textTranslated)
                    }


                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
//                save = text
                            translator.translate(text)
                                .addOnSuccessListener { translatedText ->
                                    textTranslated = translatedText

                                }

                        },
                        enabled = enabled,
                        modifier = Modifier.size(280.dp, 40.dp)

                    ) {
                        Text("번역")
                    }
                }
            }
        }
    }
}