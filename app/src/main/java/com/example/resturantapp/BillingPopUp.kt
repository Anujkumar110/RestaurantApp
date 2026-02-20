package com.example.resturantapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.resturantapp.roomdbcustomer.BillEntity
import com.example.resturantapp.roomdbcustomer.CustomerDatabase
import com.example.resturantapp.roomdbcustomer.CustomerEntity
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingPopUpScreen(navController: NavController) {

    val context = LocalContext.current
    val database = CustomerDatabase.getInstance(context)

    val customerDao = database?.customerDao()
    val foodDao = database?.foodDao()

    val customers = customerDao
        ?.getCustomer()
        ?.collectAsState(initial = emptyList())

    val foods = foodDao
        ?.getFood()
        ?.collectAsState(initial = emptyList())

    var expandedCustomer by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerEntity?>(null) }

    var showFoodDialog by remember { mutableStateOf(false) }
    var expandedFood by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<FoodEntity?>(null) }

    val selectedFoodList = remember { mutableStateListOf<FoodEntity>() }

    // ✅ TOTAL CALCULATION
    val totalAmount = selectedFoodList.sumOf { it.price?.toDouble() ?: 0.0 }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // ---------------- CUSTOMER DROPDOWN ----------------

            ExposedDropdownMenuBox(
                expanded = expandedCustomer,
                onExpandedChange = { expandedCustomer = !expandedCustomer }
            ) {

                TextField(
                    value = selectedCustomer?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Customer") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedCustomer,
                    onDismissRequest = { expandedCustomer = false }
                ) {

                    customers?.value?.forEach { customer ->
                        DropdownMenuItem(
                            text = { Text(customer.name ?: "") },
                            onClick = {
                                selectedCustomer = customer
                                expandedCustomer = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // ---------------- SELECTED CUSTOMER ----------------
//
//            selectedCustomer?.let { customer ->
//
//                Text("Selected Customer")
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(6.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(12.dp)
//                    ) {
//                        Text("Name: ${customer.name}")
//                        Text("Contact: ${customer.contact}")
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))

            // ---------------- FOOD LIST ----------------

            Text("Selected Food Items")

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(selectedFoodList) { food ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text("Dish: ${food.dish}")
                            Text("Price: ₹${food.price}")
                        }
                    }
                }
            }

            // ---------------- TOTAL CART VALUE ----------------

            Text(
                text = "Total Cart Value: ₹$totalAmount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            // ---------------- ADD TO CART BUTTON ----------------

            Button(
                onClick = {

                    val billDao = database?.billDao()

                    val customer = selectedCustomer
                    val foodList = selectedFoodList

                    if (customer != null && foodList.isNotEmpty()) {

                        val bill = BillEntity(
                            customerName = customer.name ?: "",
                            customerContact = customer.contact ?: "",
                            totalAmount = totalAmount
                        )

                        GlobalScope.launch {
                            billDao?.insertBill(bill)
                        }

                        navController.navigate("bill") {
                            popUpTo("billingPopUp") {
                                inclusive = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add To Cart")
                    
            }
        }

        // ---------------- FLOATING ACTION BUTTON ----------------

        FloatingActionButton(
            onClick = {
                showFoodDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp).padding(15.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "")
        }
    }

    // ---------------- FOOD SELECTION DIALOG ----------------

    if (showFoodDialog) {

        Dialog(
            onDismissRequest = { showFoodDialog = false }
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text("Select Food Item")

                    ExposedDropdownMenuBox(
                        expanded = expandedFood,
                        onExpandedChange = { expandedFood = !expandedFood }
                    ) {

                        TextField(
                            value = selectedFood?.dish ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Food") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFood,
                            onDismissRequest = { expandedFood = false }
                        ) {

                            foods?.value?.forEach { food ->

                                DropdownMenuItem(
                                    text = {
                                        Text("${food.dish} - ₹${food.price}")
                                    },
                                    onClick = {
                                        selectedFood = food
                                        expandedFood = false
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            selectedFood?.let {
                                selectedFoodList.add(it)
                            }
                            showFoodDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("ADD")
                    }
                }
            }
        }
    }
}
