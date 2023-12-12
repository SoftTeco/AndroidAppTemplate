package com.softteco.template.ui.feature.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.Constants.CHARTS_STEP_VALUE
import com.softteco.template.Constants.SPACE_STRING
import com.softteco.template.MainActivity
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingExtraLarge
import com.softteco.template.utils.generateRandomColor
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView

private lateinit var activity: MainActivity
private val tempValues: MutableList<PointValue> = ArrayList()
private val humValues: MutableList<PointValue> = ArrayList()
private val lines: MutableList<Line> = ArrayList()
private var temperatureIndex = 0.0F
private var humidityIndex = 0.0F
val data = LineChartData()

@Composable
fun ChartScreen(
    onBackClicked: () -> Unit,
    viewModel: ChartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    activity = LocalContext.current as MainActivity

    viewModel.provideOnDeviceResultCallback {
        viewModel.provideDataLYWSD03MMC(it)
    }

    ScreenContent(onBackClicked, LocalContext.current as MainActivity, state)
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    activity: MainActivity,
    state: ChartViewModel.State,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTopAppBar(
            stringResource(id = R.string.chart),
            showBackIcon = true,
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked,
        )

        Charts(state, activity)
    }
}

@Composable
fun Charts(
    state: ChartViewModel.State,
    activity: MainActivity,
    modifier: Modifier = Modifier
) {
    val lineChartView by remember { mutableStateOf(LineChartView(activity)) }
    val tmpColor by remember { mutableIntStateOf(generateRandomColor()) }
    val humColor by remember { mutableIntStateOf(generateRandomColor()) }

    Column(
        modifier = modifier.padding(start = Dimens.PaddingDefault)
    ) {
        Text(
            text = stringResource(R.string.temperature).plus(SPACE_STRING)
                .plus(state.dataLYWSD03MMC.temperature.toString()).plus(SPACE_STRING)
                .plus(stringResource(R.string.temperature_degrees))
        )
        Text(
            text = stringResource(R.string.humidity).plus(SPACE_STRING)
                .plus(state.dataLYWSD03MMC.humidity.toString()).plus(SPACE_STRING)
                .plus(stringResource(R.string.humidity_percent))
        )
        Text(
            text = stringResource(R.string.battery).plus(SPACE_STRING)
                .plus(state.dataLYWSD03MMC.battery.toString()).plus(SPACE_STRING)
                .plus(stringResource(R.string.battery_voltage))
        )
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = PaddingExtraLarge, end = PaddingExtraLarge, bottom = PaddingDefault),
            factory = {
                lineChartView
            }
        )
    }

    state.dataLYWSD03MMC.let {
        tempValues.add(
            PointValue(temperatureIndex, it.temperature.toFloat()).setLabel(
                stringResource(R.string.temperature_point_label)
            )
        )
        humValues.add(
            PointValue(
                humidityIndex,
                it.humidity.toFloat()
            ).setLabel(stringResource(R.string.humidity_point_label))
        )

        temperatureIndex += CHARTS_STEP_VALUE
        humidityIndex += CHARTS_STEP_VALUE

        lines.add(Line(tempValues).setColor(tmpColor).setHasLabels(true))
        lines.add(Line(humValues).setColor(humColor).setHasLabels(true))

        data.axisYLeft = Axis()
        data.axisXBottom = Axis()
        data.lines = lines

        lineChartView.lineChartData = data
    }
}
