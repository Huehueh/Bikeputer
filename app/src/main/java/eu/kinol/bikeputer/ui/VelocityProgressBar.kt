package eu.kinol.bikeputer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val Velocity1 = Color(0xFF00C980)
val Velocity2 = Color(0xFFFFC114)
val Velocity3 = Color(0xFFFF4646)
val Velocity4 = Color(0xFF770000)


fun velocityColor(velocity: Double): Color {
    return when (velocity) {
        in 0.0..10.0 -> Velocity1
        in 10.0..20.0 -> Velocity2
        in 20.0..30.0 -> Velocity3
        else -> Velocity4
    }
}

fun previousVelocityColor(velocity: Double): Color {
    val previous = velocity - 10f
    if (previous < 0f) {
        return Color.Transparent
    }
    return velocityColor(previous)
}


@ExperimentalAnimationApi
@Composable
fun VelocityProgressBar(
    bikeData: BikeData,
    radius: Dp = 100.dp
){
    val velocity by bikeData.velocity

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(
            modifier = Modifier.size(radius * 2f)
        ) {
            drawArc(
                color = velocityColor(velocity!!),
                startAngle = -270f,
                sweepAngle = (36f * velocity!!.mod(10f)).toFloat(),
                useCenter = false,
                style = Stroke(width = 60f, cap = StrokeCap.Square)
            )
            drawArc(
                color = previousVelocityColor(velocity!!),
                startAngle = (- 270f + 36f * velocity!!.mod(10f)).toFloat(),
                sweepAngle = (360f - 36f * velocity!!.mod(10f)).toFloat(),
                useCenter = false,
                style = Stroke(width = 60f, cap = StrokeCap.Square)
            )
        }
        Text(
            text = String.format("%.1f", velocity) + " km/h",
            color = Color.Black,
            fontSize = 30.sp,
            maxLines = 1
        )
    }


}

@Preview
@Composable
@ExperimentalAnimationApi
fun VelocityProgressBarPreview() {

    VelocityProgressBar(
        bikeData = BikeData(),
        radius = 200.dp
    )
}