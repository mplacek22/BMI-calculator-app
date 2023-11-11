package com.placek.maja.bmi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selectedMode: UnitMode, updateMode: (UnitMode) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        UnitMode.values().forEach {
            ElevatedFilterChip(selectedMode == it, onClick = { updateMode(it) },
                label = {
                    Text(it.name)
                }
            )
        }
    }
}


@Composable
fun RowScope.ActionButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    val focusManager = LocalFocusManager.current
    Button(
        onClick = { focusManager.clearFocus(); onClick() },
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(14.dp)
    ) {
        Text(text, fontSize = 15.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(state: ValueState, imeAction: ImeAction, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = state.value,
        isError = state.error != null,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = { Text(text = state.label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        trailingIcon = {
            Text(text = state.suffix)
        }
    )
}


@Composable
fun MainScreen(navController: NavController, viewModel: BMIViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        TopAppBarWithMenu(navController, viewModel)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ModeSelector(viewModel.selectedUnitMode, updateMode = viewModel::updateMode)
                CustomTextField(viewModel.heightState, ImeAction.Next, viewModel::updateHeight)
                CustomTextField(viewModel.weightState, ImeAction.Done, viewModel::updateWeight)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "%.2f".format(viewModel.bmi),
                    style = MaterialTheme.typography.headlineSmall,
                    color = getBMIColor(bmi = viewModel.bmi)
                )
                Divider(modifier = Modifier.fillMaxWidth(.7f), thickness = 2.5.dp)
                Text(
                    text = viewModel.result,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Button(
                    onClick = { /*TODO*/ },
                    enabled = viewModel.canCalculate()
                ) {
                    Text("View details", fontSize = 15.sp)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ActionButton(text = "Clear", viewModel::clear)
                ActionButton(text = "Calculate", viewModel::calculate, viewModel.canCalculate())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    navController: NavController,
    viewModel: BMIViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = "BMI Calculator") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(text =  { Text(text = stringResource(R.string.about_author)) }, onClick = {
                    navController.navigate(Screen.AboutAuthorScreen.route)
                })
                DropdownMenuItem(text = { Text(text = stringResource(R.string.history)) }, onClick = {
                    navController.navigate(Screen.BmiDescriptionScreen.withArgs(viewModel.bmi.toString()))
                })
            }
        }
    )
}

@Composable
fun getBMIColor(bmi: Double): Color {
    return when {
        bmi == 0.0 -> MaterialTheme.colorScheme.primary
        bmi < 18.5 -> Color.Red // Underweight
        bmi < 24.9 -> Color.Green // Normal weight
        bmi < 29.9 -> Color.Yellow // Overweight
        else -> Color.Red // Obese
    }
}