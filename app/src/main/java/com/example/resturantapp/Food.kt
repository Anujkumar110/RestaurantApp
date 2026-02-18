package com.example.resturantapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resturantapp.roomdbcustomer.CustomerDatabase
import com.example.resturantapp.roomdbcustomer.FoodEntity
import kotlinx.coroutines.launch

class Food : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Preview(showSystemUi = true)

@Composable
fun FoodScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val database = CustomerDatabase.getInstance(context)
    val foodDao = database?.foodDao()

    val foods = foodDao?.getFood()?.collectAsState(initial = emptyList())

    var selectedFood by remember { mutableStateOf<FoodEntity?>(null) }



    Box(Modifier.fillMaxSize()){

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp))
        {

            LazyColumn(modifier = Modifier.fillMaxSize())

            {
                items(foods?.value!!.size)
                {
                    index ->
                    val food = foods?.value!![index]

                    Card ( Modifier.fillMaxWidth().padding(10.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp ),
                        shape = RoundedCornerShape(7.dp)
                        ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FoodIcons (
                                food = food,
                                onDelete = {
                                    scope.launch {
                                        foodDao?.deleteFood(food)
                                    }
                                },
                                onEdit = {
                                    selectedFood = food
                                    showDialog = true

                                }
                            )
                        }

                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = ""
            )
        }
    }

    FoodDialog(
        showDialog = showDialog,
        food = selectedFood,
        dismiss = {
            showDialog = false
            selectedFood = null
        },
        foodDao = foodDao
    )


}













