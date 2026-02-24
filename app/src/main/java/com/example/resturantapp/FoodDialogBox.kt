package com.example.resturantapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.resturantapp.roomdbcustomer.FoodDao
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FoodDialog(
    showDialog: Boolean,
    food: FoodEntity?,
    dismiss: () -> Unit,
    foodDao: FoodDao?
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var dish by remember { mutableStateOf(food?.dish ?: "") }
    var price by remember { mutableStateOf(food?.price?.toString() ?: "") }

    if (showDialog) {

        Dialog(
            properties = DialogProperties(dismissOnClickOutside = true),
            onDismissRequest = dismiss
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (food == null) "Add Item" else "Update Item",
                    fontSize = 22.sp
                )

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = dish,
                    onValueChange = { dish = it },
                    singleLine = true,
                    label = { Text("Dish Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )

                Spacer(Modifier.height(12.dp))

                TextField(
                    value = price,
                    onValueChange = { price = it },
                    singleLine = true,
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )

                Spacer(Modifier.height(20.dp))

                ElevatedButton(
                    onClick = {

                        if (dish.isBlank()) {
                            Toast.makeText(context, "Enter Dish Name", Toast.LENGTH_SHORT).show()
                            return@ElevatedButton
                        }

                        val priceValue = price.toDoubleOrNull()

                        if (priceValue == null) {
                            Toast.makeText(context, "Enter Valid Price", Toast.LENGTH_SHORT).show()
                            return@ElevatedButton
                        }

                        scope.launch {
                            withContext(Dispatchers.IO) {

                                if (food == null) {
                                    // ADD
                                    foodDao?.addFood(
                                        FoodEntity(
                                            dish = dish,
                                            price = priceValue
                                        )
                                    )
                                } else {
                                    // UPDATE
                                    foodDao?.updateFood(
                                        food.copy(
                                            dish = dish,
                                            price = priceValue
                                        )
                                    )
                                }
                            }

                            dismiss()

                            Toast.makeText(
                                context,
                                if (food == null) "Food Saved" else "Food Updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save")
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}