package com.example.resturantapp.roomdbcustomer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CustomerDao {

    @Insert
    suspend fun addCustomer(customerEntity: CustomerEntity)

    @Query("select * from customer_table")
    fun getCustomer() : Flow<List<CustomerEntity>>

}

@Dao
interface FoodDao {

    @Insert
    suspend fun addFood (foodEntity: FoodEntity)

    @Query("select * from food_table")
    fun getFood() : Flow<List<FoodEntity>>
}