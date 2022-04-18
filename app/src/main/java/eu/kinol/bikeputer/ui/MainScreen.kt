package eu.kinol.bikeputer

import android.graphics.Color
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@ExperimentalAnimationApi
@Composable
fun MainScreen(
    bikeData: BikeData,
    navigateToSettings: () ->Unit,
    navigateToStats:() -> Unit
) {
    Scaffold(
        topBar = {
            MainAppBar(
                onSettingsClicked = navigateToSettings,
                onStatsClicked = navigateToStats
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
                    TurnSignalWidget(bikeData = bikeData)
                    BatteryLevelScreen(batteryLevel = bikeData.betteryLevel)
                    
//                }
                VelocityScreen(bikeData = bikeData)

                var color = androidx.compose.ui.graphics.Color.Red
                if(bikeData.connected.value) {
                    color =  androidx.compose.ui.graphics.Color.Green
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(15.dp)
                    .background(color))
            }

        }
    )
}

@ExperimentalAnimationApi
@Composable
@Preview
fun MainScreenPreview() {
    var bikeData = BikeData()
    bikeData.betteryLevel.value = 10
    bikeData.velocity.value = 10.0
    bikeData.connected.value = true
    bikeData.updateTurnSignal(3)

    MainScreen(bikeData = bikeData, {}, {})
}