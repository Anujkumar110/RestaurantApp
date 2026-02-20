package com.example.resturantapp.roomdbcustomer

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "customer_table")
data class CustomerEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String? = null,
    var contact: String? = null,
)


@Entity(tableName = "food_table")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var dish : String? = null,
    var price : String? = null
)


@Entity(
    tableName = "bill_table",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId")]
)
data class BillEntity(

    @PrimaryKey(autoGenerate = true)
    val billId: Int = 0,

    val customerId: Int,
    val totalAmount: Double
)