package eu.kinol.bikeputer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.kinol.bikeputer.ui.Statistics

@ExperimentalAnimationApi
@Composable
fun VelocityScreen(bikeData: BikeData) {


    val stats = Statistics()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))
        VelocityProgressBar(bikeData = bikeData)
        Spacer(modifier = Modifier.height(30.dp))

        BikeStatTable(stats = stats)

        Spacer(modifier = Modifier.height(30.dp))
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.end_route))
        }
        
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun VelocityScreenPreview() {
    VelocityScreen(BikeData())
}