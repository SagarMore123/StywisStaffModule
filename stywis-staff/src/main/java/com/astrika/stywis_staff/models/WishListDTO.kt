package com.astrika.stywis_staff.models

data class WishListDTO(
    var wishlistId: Long?,
    var userId: Long?,
    var items: ArrayList<WishListItems>? = arrayListOf(),
    var wishlistName: String?,
    var status: String?,
    var active: Boolean?
)


data class WishListItems(
    var itemId: Long?,
    var quantity: Long?,
    var remarks: String?,
    var itemText: String?,
    var status: String?,
    var active: Boolean?,
    var selected: Boolean = false //for UI

)

enum class WishListItemStatus {
    PENDING,
    ORDERED,
    COMPLETED
}
