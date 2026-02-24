package com.example.resturantapp.roomdbcustomer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.resturantapp.roomdbcustomer.CustomerEntity
import com.example.resturantapp.roomdbcustomer.FoodEntity
import com.example.resturantapp.roomdbcustomer.BillEntity
import com.example.resturantapp.roomdbcustomer.BillItemEntity


@Database(
    entities = [CustomerEntity::class, FoodEntity::class, BillEntity::class, BillItemEntity::class],
    version = 4,
    exportSchema = false
)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun foodDao(): FoodDao
    abstract fun billDao(): BillDao
    abstract fun billItemDao(): BillItemDao


    companion object {

        @Volatile
        private var INSTANCE: CustomerDatabase? = null

        fun getInstance(context: Context): CustomerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CustomerDatabase::class.java,
                    "customer_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
