package com.placek.maja.bmi.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.placek.maja.bmi.R
import com.placek.maja.bmi.composables.getBMIColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, history: List<String>?) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.bmi_calculation_history))
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )
        if (history != null) {
            if (history.isNotEmpty()) {
                BMIHistoryTable(bmiHistory = history)
            } else {
                Text("No history available.")
            }
        }
    }
}
@Composable
fun BMIHistoryTable(bmiHistory: List<String>) {
    LazyColumn {
        items(bmiHistory.toList()) { bmiEntry ->
            val (date, bmi, weight, height) = parseBMIData(bmiEntry)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.medium_space)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(date, style = MaterialTheme.typography.bodyMedium)
                    val weightStr = stringResource(id = R.string.weight)
                    Text("$weightStr: $weight", style = MaterialTheme.typography.bodyMedium)
                    val heightStr = stringResource(id = R.string.height)
                    Text("$heightStr: $height", style = MaterialTheme.typography.bodyMedium)
                }
                Column {
                    Text(bmi, style = MaterialTheme.typography.headlineMedium, color = getBMIColor(bmi = bmi.toDouble()))
                }
            }
        }
    }
}


@Composable
fun parseBMIData(data: String): BMIHistoryEntry {
    val parts = data.split(",")
    return if (parts.size == 4) {
        val date = parts[0].trim()
        val bmi = parts[1].trim()
        val weight = parts[2].trim()
        val height = parts[3].trim()
        BMIHistoryEntry(date, bmi, weight, height)
    } else {
        BMIHistoryEntry("", "0.0", "0.0", "0.0")
    }
}

data class BMIHistoryEntry(
    val date: String,
    val bmi: String,
    val weight: String,
    val height: String,
)