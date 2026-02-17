package com.example.resturantapp.roomdbfood

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//
//@Database(entities = [FoodEntity::class], version = 1,exportSchema = false)
//abstract class FoodDatabase: RoomDatabase() {
//
//    abstract fun foodDao(): FoodDao
//    companion object {
//        private var INSTANCE: FoodDatabase? = null
//        fun getInstance(context: Context): FoodDatabase? {
//            synchronized(this) {
//                println("Check GetInstance of Room")
//                var instance = INSTANCE
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context,
//                        FoodDatabase::class.java,
//                        "student_database"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                }
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}