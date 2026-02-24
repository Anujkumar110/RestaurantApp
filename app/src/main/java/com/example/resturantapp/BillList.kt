package com.example.resturantapp

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
import androidx.navigation.NavController
import com.example.resturantapp.roomdbcustomer.CustomerDatabase

@Composable
fun BillListScreen(navController: NavController) {

    val context = LocalContext.current
    val database = CustomerDatabase.getInstance(context)
    val billDao = database.billDao()

    val bills by billDao
        .getAllBillsWithCustomer()
        .collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            items(bills) { bill ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    onClick = {
                        navController.navigate("bill/${bill.bill.billId}")
                    }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = bill.customer.name ?: "",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Total: ₹${bill.bill.totalAmount}")
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("billingPopUp")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "")
        }
    }
}