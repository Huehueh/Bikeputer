package eu.kinol.bikeputer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.kinol.bikeputer.ui.Statistics

@Composable
fun BikeStatTable(stats: Statistics) {
    val column1Weight = .5f // 30%
    val column2Weight = .5f // 70%
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {

        val statMap: Map<String, Any> = stats.asMap()

        items(statMap.toList()) {
            val (key, value) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(
                    text = key,
                    weight = column1Weight,
                    textAlignment = TextAlign.End
                )
                TableCell(
                    text = value.toString(),
                    weight = column2Weight,
                    textAlignment = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    textAlignment: TextAlign
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        textAlign = textAlignment,
        maxLines = 1
    )
}

@Preview
@Composable
fun BikeStatTablePreview() {
    val stats = Statistics(maxVelocity = 15f, avVelocity = 10f, distance = 9234f)
    BikeStatTable(stats = stats)
}