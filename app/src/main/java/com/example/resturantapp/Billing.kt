package com.example.resturantapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.resturantapp.roomdbcustomer.CustomerDatabase

class Billing : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}




//@Composable
//fun BillingScreen(navController: NavController, billId: Int) {
//
//    val context = LocalContext.current
//    val database = CustomerDatabase.getInstance(context)
//    val billDao = database?.billDao()
//
//    val billState = billDao
//        ?.getBillWithItems(billId)
//        ?.collectAsState(initial = null)
//
//    val billData = billState?.value
//
//    billData?.let { data ->
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//
//            Text(
//                "Order ID: ${data.bill.billId}",
//                style = MaterialTheme.typography.titleLarge
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text("Ordered By: ${data.customer.name}")
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            Text("Items:")
//
//            data.items.forEach { item ->
//                Text("${item.food.dish} - ₹${item.billItem.price}")
//            }
//
//            Spacer(modifier = Modifier.height(15.dp))
//
//            Text(
//                "Total: ₹${data.bill.totalAmount}",
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        FloatingActionButton(
//            onClick = {
//                navController.navigate("billingPopUp")
//            },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(10.dp)
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "")
//        }
//    }
//}

@Composable
fun BillingScreen(navController: NavController, billId: Int) {

    val context = LocalContext.current
    val database = CustomerDatabase.getInstance(context)
    val billDao = database.billDao()

    val billData by billDao
        .getBillWithItems(billId)
        .collectAsState(initial = null)

    billData?.let { data ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                "Order ID: ${data.bill.billId}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Ordered By: ${data.customer.name}")

            Spacer(modifier = Modifier.height(15.dp))

            Text("Items:")

            data.items.forEach { item ->
                Text("${item.food.dish} - ₹${item.billItem.price}")
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Total: ₹${data.bill.totalAmount}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                navController.navigate("billList")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "")
        }
    }
}


