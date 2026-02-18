package com.example.resturantapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.resturantapp.roomdbcustomer.CustomerDatabase
import com.example.resturantapp.roomdbcustomer.FoodDao
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showSystemUi = true)

    @Composable
    fun DashboardScreen() {

        var selectedIndex by remember { mutableStateOf(0) }
        var navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var currentRoot = navBackStackEntry?.destination?.route ?: "bill"

        selectedIndex = when (currentRoot) {
            "bill" -> 0
            "customer" -> 1
            "food" -> 2
            else -> 0
        }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Dashboard", color = Color.White)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Blue
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Blue
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column() {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "",
                                tint = if (selectedIndex == 0)
                                    Color.White
                                else
                                    Color.Black,
                                modifier = Modifier.size(40.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                    ) {
                                        navController.navigate("bill") {

                                        }
                                    }
                            )
                            if (selectedIndex == 0) {
                                Text("Billig", color = Color.White)
                            }
                        }

                        Column() {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "",
                                tint = if (selectedIndex == 1)
                                    Color.White
                                else
                                    Color.Black,
                                modifier = Modifier.size(40.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        navController.navigate("customer")
                                    }
                            )
                            if (selectedIndex == 1) {
                                Text("Customer", color = Color.White)
                            }
                        }

                        Column() {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "",
                                tint = if (selectedIndex == 2)
                                    Color.White
                                else
                                    Color.Black,
                                modifier = Modifier.size(40.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        navController.navigate("food") {

                                        }
                                    }
                            )
                            if (selectedIndex == 2) {
                                Text("Food", color = Color.White)
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "bill",
                modifier = Modifier.fillMaxWidth().padding(innerPadding)
            )
            {
                composable("bill") {
                    BillingScreen()
                }
                composable("customer") {
                    CustomerScreen()
                }
                composable("food") {
                    FoodScreen()
                }
            }
        }
    }
}




