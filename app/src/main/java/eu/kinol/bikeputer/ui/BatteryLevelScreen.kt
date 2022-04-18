package eu.kinol.bikeputer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BatteryLevelScreen(
    batteryLevel: MutableState<Long>
) {
    Box(
        modifier = Modifier.padding(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text =  "${batteryLevel.value}%",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1
        )

        Icon(
            painter = painterResource(id = R.drawable.battery),
            contentDescription = "stan baterii",
            modifier = Modifier.size(50.dp)

        )
    }
}

@Preview
@Composable
fun BatteryLevelScreenPreview() {
    BatteryLevelScreen(batteryLevel = remember {
        mutableStateOf(50)
    })
}