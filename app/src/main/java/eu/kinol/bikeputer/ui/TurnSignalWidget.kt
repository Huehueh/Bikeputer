package eu.kinol.bikeputer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


fun showLeft(turnSignal: TurnSignal):Boolean {
    return (turnSignal == TurnSignal.LEFT || turnSignal == TurnSignal.BOTH)
}

fun showRight(turnSignal: TurnSignal):Boolean {
    return (turnSignal == TurnSignal.RIGHT || turnSignal == TurnSignal.BOTH)
}

@ExperimentalAnimationApi
@Composable
fun TurnSignalWidget(bikeData: BikeData) {
    val turnSignal:TurnSignal by bikeData.turnSignal
    Row(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = showLeft(turnSignal = turnSignal)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "arrow_left"
            )
        }
        AnimatedVisibility(
            visible = showRight(turnSignal = turnSignal)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "arrow_right"
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
@Preview
fun TurnSignalWidgetPreview() {
    var bikeData = BikeData()
    bikeData.updateTurnSignal(3)
    TurnSignalWidget(bikeData = bikeData)
}