package com.example.resturantapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.resturantapp.roomdbcustomer.BillEntity
import com.example.resturantapp.roomdbcustomer.BillItemEntity
import com.example.resturantapp.roomdbcustomer.CustomerDatabase
import com.example.resturantapp.roomdbcustomer.CustomerEntity
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingPopUpScreen(navController: NavController) {

    val context = LocalContext.current
    val database = CustomerDatabase.getInstance(context)

    val scope = rememberCoroutineScope()

    val customerDao = database.customerDao()
    val foodDao = database.foodDao()

    val customers = customerDao
        .getCustomer()
        .collectAsState(initial = emptyList())

    val foods = foodDao
        .getFood()
        .collectAsState(initial = emptyList())

    var expandedCustomer by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerEntity?>(null) }

    var showFoodDialog by remember { mutableStateOf(false) }
    var expandedFood by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<FoodEntity?>(null) }

    val selectedFoodList = remember { mutableStateListOf<FoodEntity>() }

    val totalAmount = selectedFoodList.sumOf { it.price?.toDouble() ?: 0.0 }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // ================= CUSTOMER DROPDOWN =================

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

                    customers.value.forEach { customer ->
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

            Text(
                text = "Total Cart Value: ₹$totalAmount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            // ================= ADD TO CART BUTTON =================

            Button(
                onClick = {

                    val customer = selectedCustomer
                    val foodList = selectedFoodList

                    if (customer?.id != null && foodList.isNotEmpty()) {

                        val billDao = database.billDao()
                        val billItemDao = database.billItemDao()

                        val total = foodList.sumOf { it.price?.toDouble() ?: 0.0 }

                        scope.launch {

                            // Insert Bill
                            val billIdLong = withContext(Dispatchers.IO) {
                                billDao.insertBill(
                                    BillEntity(
                                        customerId = customer.id,
                                        totalAmount = total
                                    )
                                )
                            }

                            val billIdInt = billIdLong.toInt()

                            // Insert Bill Items
                            withContext(Dispatchers.IO) {
                                foodList.forEach { food ->
                                    if (food.id != null) {
                                        billItemDao.insertBillItem(
                                            BillItemEntity(
                                                billId = billIdInt,
                                                foodId = food.id,
                                                quantity = 1,
                                                price = food.price?.toDouble() ?: 0.0
                                            )
                                        )
                                    }
                                }
                            }

                            // Navigate safely on Main thread
                            navController.navigate("bill")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add To Order")
            }
        }

        // ================= FLOATING BUTTON =================

        FloatingActionButton(
            onClick = { showFoodDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp)
                .padding(15.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "")
        }
    }

    // ================= FOOD DIALOG =================

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

                            foods.value.forEach { food ->
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