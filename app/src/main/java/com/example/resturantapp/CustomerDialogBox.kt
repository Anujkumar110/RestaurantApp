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
import com.example.resturantapp.roomdbcustomer.CustomerDao
import com.example.resturantapp.roomdbcustomer.CustomerEntity
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomerDialogBox: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}


@Composable
fun CustomerDialog(
    showDialog: Boolean,
    customer: CustomerEntity?,
    dismiss: () -> Unit,
    customerDao: CustomerDao?
) {

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
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

                    Text(
                        text = if (customer == null) "Add Customer:" else "Update Customer:",
                        fontSize = 25.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    TextField(
                        value = contact,
                        onValueChange = { contact = it },
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    ElevatedButton(
                        onClick = {
                            if (name.isEmpty()) {
                                Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show()
                            }
                            else if (contact.isEmpty()) {
                                Toast.makeText(context, "Enter Contact", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                scope.launch {
                                    withContext(Dispatchers.IO) {

                                        if (customer == null) {
                                            customerDao?.addCustomer(
                                                CustomerEntity(
                                                    name = name,
                                                    contact = contact
                                                )
                                            )
                                        } else {
                                            customerDao?.updateCustomer(
                                                customer.copy(
                                                    name = name,
                                                    contact = contact
                                                )
                                            )
                                        }
                                    }

                                    dismiss()

                                    Toast.makeText(
                                        context,
                                        if (customer == null) "Customer Saved" else "Customer Updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
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