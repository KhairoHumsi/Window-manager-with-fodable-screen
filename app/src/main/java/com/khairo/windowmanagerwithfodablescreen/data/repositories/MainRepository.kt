package com.khairo.windowmanagerwithfodablescreen.data.repositories

import com.khairo.windowmanagerwithfodablescreen.data.enums.Operations
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.data.models.CategoriesModel
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel

abstract class MainRepository {

    protected val categories: List<CategoriesModel> = listOf(
        CategoriesModel(1, "test 1"),
        CategoriesModel(2, "test 2"),
        CategoriesModel(3, "test 3")
    )

    protected val items: List<ItemModel> = listOf(
        ItemModel(1, 1, "test 1 sjfb dllbvlaeflkdnalbelkg sdhvfzdsv bkasn v.adf", 4.5f),
        ItemModel(2, 2, "test 2 sjfb dllbvlaeflkdnalbelkg", 2f),
        ItemModel(3, 1, "test 3", 6f),
        ItemModel(4, 1, "test 4", 3f),
        ItemModel(5, 2, "test 5", 1.5f),
        ItemModel(6, 1, "test 6", 1.25f),
        ItemModel(7, 2, "test 7", 9.15f),
        ItemModel(8, 1, "test 8", 4.53f),
        ItemModel(9, 2, "test 9", 2.9f),
        ItemModel(10, 1, "test 10", 3f),
        ItemModel(11, 2, "test 11", 43.5f)
    )

    protected val cart: ArrayList<CartModel> = ArrayList()

    protected var subTotalBill = 0f

    abstract suspend fun getCategories(): List<CategoriesModel>
    abstract suspend fun getItems(categoryId: Int): List<ItemModel>
    abstract suspend fun getCart(): List<CartModel>
    abstract suspend fun addToCart(model: ItemModel, added: suspend (List<CartModel>) -> Unit)
    abstract suspend fun checkFromItem(itemId: Int): Boolean
    abstract suspend fun increaseItem(itemId: Int, updated: suspend (List<CartModel>) -> Unit)
    abstract suspend fun decreaseItem(itemId: Int, updated: suspend (List<CartModel>) -> Unit)
    abstract suspend fun removeItem(item: CartModel, updated: suspend (List<CartModel>) -> Unit)
    abstract suspend fun clearCart()
    abstract suspend fun getSubTotalBill(): Float
    protected abstract fun updateBill(value: Float, operation: Operations)

}
