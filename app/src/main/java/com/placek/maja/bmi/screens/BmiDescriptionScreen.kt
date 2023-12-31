package com.placek.maja.bmi.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.placek.maja.bmi.R
import com.placek.maja.bmi.composables.TopAppBarWithPopBack
import com.placek.maja.bmi.composables.getBMIColor

@Composable
fun BmiDescriptionScreen(navController: NavController,bmi: Double?) {
    val description = when {
        bmi!! < 18.5 -> stringResource(R.string.underweight_description)
        bmi < 24.9 -> stringResource(R.string.healthy_weight_description)
        bmi < 29.9 -> stringResource(R.string.overweight_description)
        bmi >= 29.9 -> stringResource(R.string.obesity_description)
        else -> "Invalid BMI"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarWithPopBack(navController = navController, title = stringResource(id = R.string.bmi_result_title))

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_space)))

        val formattedBMI = String.format("%.1f", bmi)
        Text(
            text = "BMI: $formattedBMI",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier
                .background(
                    color = getBMIColor(bmi = bmi),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.small_space)),
                )
                .padding(dimensionResource(id = R.dimen.small_space))
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_space)))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_space)))

        BMITable()
    }
}

@Composable
fun BMITable() {
    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.medium_space)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.bmi_range), style = MaterialTheme.typography.headlineMedium)
                Text(stringResource(R.string.category), style = MaterialTheme.typography.headlineMedium)
            }
        }
        items(bmiData) { bmiEntry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.medium_space)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(bmiEntry.range, style = MaterialTheme.typography.bodyMedium)
                Text(stringResource(id = bmiEntry.categoryResId), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

data class BMIData(val range: String, @StringRes val categoryResId: Int)

val bmiData = listOf(
    BMIData("< 18.5", R.string.underweight),
    BMIData("18.5 – 24.9", R.string.healthy_weight),
    BMIData("25 – 29.9", R.string.overweight),
    BMIData("> 30", R.string.obesity)
)