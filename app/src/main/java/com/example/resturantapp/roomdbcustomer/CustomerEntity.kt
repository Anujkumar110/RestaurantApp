package com.example.resturantapp.roomdbcustomer

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation


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
    val id: Long = 0,

    val dish: String,

    val price: Double
)


//@Entity(tableName = "bill_table")
//data class BillEntity(
//
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//
//    val customerName: String,
//    val customerContact: String,
//    val totalAmount: Double
//)


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
    val billId: Long = 0,

    val customerId: Long,
    val totalAmount: Double
)

@Entity(
    tableName = "bill_item_table",
    foreignKeys = [
        ForeignKey(
            entity = BillEntity::class,
            parentColumns = ["billId"],
            childColumns = ["billId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("billId"), Index("foodId")]
)
data class BillItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val billId: Int,
    val foodId: Long,
    val quantity: Int,
    val price: Double
)

//data class BillItemWithFood(
//
//    @Embedded
//    val billItem: BillItemEntity,
//
//    @Relation(
//        parentColumn = "foodId",
//        entityColumn = "id"
//    )
//    val food: FoodEntity
//)

//data class BillWithItems(
//
//    @Embedded
//    val bill: BillEntity,
//
//    @Relation(
//        parentColumn = "customerId",
//        entityColumn = "id"
//    )
//    val customer: CustomerEntity,
//
//    @Relation(
//        entity = BillItemEntity::class,
//        parentColumn = "billId",
//        entityColumn = "billId"
//    )
//    val items: List<BillItemWithFood>
//)

data class BillWithCustomer(

    @Embedded
    val bill: BillEntity,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "id"
    )
    val customer: CustomerEntity
)

data class BillItemWithFood(

    @Embedded
    val billItem: BillItemEntity,

    @Relation(
        parentColumn = "foodId",
        entityColumn = "id"
    )
    val food: FoodEntity
)

data class BillWithItems(

    @Embedded
    val bill: BillEntity,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "id"
    )
    val customer: CustomerEntity,

    @Relation(
        entity = BillItemEntity::class,
        parentColumn = "billId",
        entityColumn = "billId"
    )
    val items: List<BillItemWithFood>
)