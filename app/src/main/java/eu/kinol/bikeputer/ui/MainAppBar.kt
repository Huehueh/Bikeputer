package eu.kinol.bikeputer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun MainAppBar(
    onSettingsClicked:()->Unit,
    onStatsClicked:()-> Unit
) {
    TopAppBar (
        navigationIcon = {
            SettingsAction(onSettingsClicked = onSettingsClicked)
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                maxLines = 1,
                color = MaterialTheme.colors.primary
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        actions = {
            StatisticsAction(onStatsClicked = onStatsClicked)

        }
    )
}



@Composable
fun SettingsAction(
    onSettingsClicked: () -> Unit
) {
    IconButton(
        onClick = onSettingsClicked
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(id = R.string.settings),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun StatisticsAction(
    onStatsClicked: () -> Unit
) {
    IconButton(
        onClick = onStatsClicked
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_stats), 
            contentDescription = stringResource(id = R.string.statistics),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Preview
@Composable
fun MainAppBarPreview() {
    MainAppBar({}, {})
}