package com.example.resturantapp.roomdbcustomer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface CustomerDao {

    @Insert
    suspend fun addCustomer(customerEntity: CustomerEntity)

    @Update
    suspend fun updateCustomer (customerEntity: CustomerEntity)

    @Delete
    suspend fun deleteCustomer (customerEntity: CustomerEntity)

    @Query("select * from customer_table")
    fun getCustomer() : Flow<List<CustomerEntity>>

}

@Dao
interface FoodDao {

    @Insert
    suspend fun addFood (foodEntity: FoodEntity)

    @Update
    suspend fun updateFood (foodEntity: FoodEntity)

    @Delete
    suspend fun deleteFood (foodEntity: FoodEntity)

    @Query("select * from food_table")
    fun getFood() : Flow<List<FoodEntity>>
}

@Dao
interface BillDao {

    @Insert
    suspend fun insertBill(bill: BillEntity)

    @Query("SELECT * FROM bill_table")
    fun getAllBills(): Flow<List<BillEntity>>
}


