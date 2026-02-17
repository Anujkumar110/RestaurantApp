package com.example.resturantapp.roomdbcustomer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [CustomerEntity::class, FoodEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CustomerDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun foodDao(): FoodDao

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
