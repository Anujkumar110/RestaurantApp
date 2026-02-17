package com.example.resturantapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.resturantapp.roomdbcustomer.CustomerEntity
import com.example.resturantapp.roomdbcustomer.FoodDao
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodDialogBox : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun FoodDialog(
    showDialog: Boolean,
    dismiss: () -> Unit,
    foodDao: FoodDao?
) {

    var dish by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = true
            ),
            onDismissRequest = dismiss,

            content = {

                Column(
                    modifier = Modifier.fillMaxWidth().padding(10.dp).background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(20.dp))

                    Text(text = "Add Item:", fontSize = 25.sp,)

                    Spacer(Modifier.height(10.dp))

                    TextField(
                        value = dish,
                        onValueChange = { dish = it },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    TextField(
                        value = price,
                        onValueChange = { price = it },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    ElevatedButton(
                        onClick = {
                            if (dish.isEmpty()) {
                                Toast.makeText(context, "Enter Dish", Toast.LENGTH_SHORT).show()
                            }
                            else if (price.isEmpty()) {
                                Toast.makeText(context, "Enter Price", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                var food = FoodEntity(
                                    dish = dish,
                                    price = price
                                )

                                scope.launch {
                                    val data = withContext(Dispatchers.IO) {
                                        foodDao?.addFood(food)
                                    }

                                    dismiss()
                                    Toast.makeText(context,"Food Item Saved", Toast.LENGTH_SHORT).show()

                                }
                            }
                        },

                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        shape = RoundedCornerShape(7.dp)
                    )
                    {
                        Text("Save")
                    }
                }
            }
        )
    }
}