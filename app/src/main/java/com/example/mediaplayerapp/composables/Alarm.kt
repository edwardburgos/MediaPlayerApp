package com.example.mediaplayerapp.alarm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediaplayerapp.utils.AmPm
import java.util.*

@Composable
fun Alarm(
    setAlarm: (Int, Int, AmPm) -> Unit,
    navController: NavHostController
) {
    val calendar = Calendar.getInstance()
    val amPm =
        remember { mutableStateOf(if (calendar[Calendar.HOUR_OF_DAY] < 13) AmPm.AM else AmPm.PM) }
    val hour = remember { mutableStateOf((if (calendar[Calendar.HOUR_OF_DAY] < 13) calendar[Calendar.HOUR_OF_DAY] else calendar[Calendar.HOUR_OF_DAY] - 12).toString()) }
    val minute = remember { mutableStateOf(calendar[Calendar.MINUTE].toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Go Back",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                listOf(AmPm.AM, AmPm.PM).forEach { element ->
                    Row {
                        RadioButton(
                            selected = amPm.value == element,
                            onClick = { amPm.value = element },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(element.toString(), modifier = Modifier.padding(end = 16.dp))
                    }
                }
            }

            OutlinedTextField(
                value = hour.value,
                onValueChange = {
                    val newIt = it.filter { character -> character.isDigit() }
                    if (newIt.isNotEmpty()) {
                        if (newIt.toInt() < 13) {
                            hour.value = newIt
                        }
                    } else {
                        hour.value = newIt
                    }
                },
                label = { Text("Define an hour") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth()
            )

            OutlinedTextField(
                value = minute.value,
                onValueChange = {
                    val newIt = it.filter { character -> character.isDigit() }
                    if (newIt.isNotEmpty()) {
                        if (newIt.toInt() < 60) {
                            minute.value = newIt
                        }
                    } else {
                        minute.value = newIt
                    }
                },
                label = { Text("Define a minute") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()
            )

            Button(content = { Text(text = "Set alarm", modifier = Modifier.padding(vertical =  4.dp))},
                modifier = Modifier.fillMaxWidth(),
                onClick = { setAlarm(hour.value.toInt(), minute.value.toInt(), amPm.value) })
        }
    }
}