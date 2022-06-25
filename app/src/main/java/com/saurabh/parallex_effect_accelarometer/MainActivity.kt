package com.saurabh.parallex_effect_accelarometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saurabh.parallex_effect_accelarometer.ui.theme.Parallex_Effect_AccelarometerTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.collect

class MainActivity : ComponentActivity() {
    val dataManager by lazy {
        SensorDataManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parallex_Effect_AccelarometerTheme {
                // A surface container using the 'background' color from the theme
                val scope = rememberCoroutineScope()

                var data by remember { mutableStateOf<SensorData?>(null) }
                Surface(modifier = Modifier.fillMaxSize()) {


                    DisposableEffect(Unit) {

                        dataManager.init()

                        scope.launch {
                            dataManager.data
                                .receiveAsFlow()
                                .onEach { data = it }
                                .collect()
                        }

                        onDispose {
                            dataManager.cancel()
                        }
                    }

                    if (data != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center

                        ) {
                            // Blurred image as glow shadow for the card
                            // Will move in opposite direction of Image Card to reveal itself when inclined
                            Image(
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                alpha = 0.2f,
                                painter = painterResource(id = R.drawable.saurabhkumar),
                                modifier = Modifier
                                    .offset(
                                        x = (-data!!.roll * 0.5).dp,
                                        y = (data!!.pitch * 0.7).dp
                                    )
                                    .width(230.dp)
                                    .height(400.dp).shadow(elevation = 0.dp, RoundedCornerShape(16.dp))
                                    .blur(
                                        radius = 50.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            // White edge shadow (should move slightly slower than the card)
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = (data!!.roll * 0.35).dp,
                                        y = (-data!!.pitch * 0.35).dp
                                    )
                                    .width(230.dp)
                                    .height(410.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                            ) {

                            }

                            // Image Card (with slight parallax for the image itself)
                            Image(

                                painter = painterResource(id = R.drawable.saurabhkumar),
                                modifier = Modifier
                                    .offset(
                                        x = (data!!.roll * 0.4).dp,
                                        y = (-data!!.pitch * 0.4).dp
                                    )
                                    .width(230.dp)
                                    .height(400.dp)
                                    .clip(RoundedCornerShape(16.dp)).shadow(
                                        elevation = 50.dp,
                                    ) .blur(
                                        radius = 10.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    ),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                alignment = BiasAlignment(
                                    horizontalBias = -(data!!.roll * 0.001).toFloat(),
                                    verticalBias = 0f,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Parallex_Effect_AccelarometerTheme {
        Greeting("Saurabh Kumar")
    }
}