package com.example.resturantapp.roomdbcustomer

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// -------------------- CUSTOMER DAO --------------------

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomer(customerEntity: CustomerEntity)

    @Update
    suspend fun updateCustomer(customerEntity: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customerEntity: CustomerEntity)

    @Query("SELECT * FROM customer_table")
    fun getCustomer(): Flow<List<CustomerEntity>>
}


// -------------------- FOOD DAO --------------------

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(foodEntity: FoodEntity)

    @Update
    suspend fun updateFood(foodEntity: FoodEntity)

    @Delete
    suspend fun deleteFood(foodEntity: FoodEntity)

    @Query("SELECT * FROM food_table")
    fun getFood(): Flow<List<FoodEntity>>
}


// -------------------- BILL DAO --------------------

@Dao
interface BillDao {

    @Insert
    suspend fun insertBill(bill: BillEntity): Long

    @Transaction
    @Query("SELECT * FROM bill_table")
    fun getAllBillsWithCustomer(): Flow<List<BillWithCustomer>>

    @Transaction
    @Query("SELECT * FROM bill_table WHERE billId = :billId")
    fun getBillWithItems(billId: Int): Flow<BillWithItems>
}


// -------------------- BILL ITEM DAO --------------------

@Dao
interface BillItemDao {

    @Insert
    suspend fun insertBillItem(item: BillItemEntity)
}