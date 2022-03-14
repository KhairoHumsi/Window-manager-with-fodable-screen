package com.khairo.windowmanagerwithfodablescreen.data.di.impl

import com.khairo.windowmanagerwithfodablescreen.R
import com.khairo.windowmanagerwithfodablescreen.common.App
import com.khairo.windowmanagerwithfodablescreen.data.enums.Operations
import com.khairo.windowmanagerwithfodablescreen.data.enums.Operations.Minus
import com.khairo.windowmanagerwithfodablescreen.data.enums.Operations.Plus
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel
import com.khairo.windowmanagerwithfodablescreen.data.repositories.MainRepository
import com.khairo.windowmanagerwithfodablescreen.data.utils.suspendToast
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor() : MainRepository() {

    override suspend fun getCategories() = categories

    override suspend fun getItems(categoryId: Int) = items.filter { it.categoryId == categoryId }

    override suspend fun getCart() = cart

    override suspend fun addToCart(model: ItemModel, added: suspend (List<CartModel>) -> Unit) {
        model.apply {
            val cartModel = CartModel(id, name, price, 1)
            cart.add(cartModel)
            updateBill(price, Plus)
            added(getCart())
        }
    }

    override suspend fun checkFromItem(itemId: Int) = cart.find { it.itemId == itemId } != null

    override suspend fun increaseItem(itemId: Int, updated: suspend (List<CartModel>) -> Unit) {
        for (item in cart)
            if (item.itemId == itemId) {
                item.count += 1
                updateBill(item.price, Plus)
                updated(getCart())
                break
            }
    }

    override suspend fun decreaseItem(itemId: Int, updated: suspend (List<CartModel>) -> Unit) {
        for (item in cart)
            if (item.itemId == itemId) {
                if (item.count > 1) {
                    item.count -= 1
                    updateBill(item.price, Minus)
                    updated(getCart())

                } else
                    App.instance.getString(R.string.minimum_quantity).suspendToast(App.instance)

                break
            }
    }

    override suspend fun removeItem(item: CartModel, updated: suspend (List<CartModel>) -> Unit) {
        cart.remove(item)
        updateBill(item.price * item.count, Minus)
        updated(getCart())

    }

    override suspend fun clearCart() {
        cart.clear()
        updateBill(subTotalBill, Minus)
    }

    override suspend fun getSubTotalBill() = subTotalBill

    override fun updateBill(value: Float, operation: Operations) {
        when (operation) {
            Plus -> subTotalBill += value
            Minus -> subTotalBill -= value
        }
    }
}
