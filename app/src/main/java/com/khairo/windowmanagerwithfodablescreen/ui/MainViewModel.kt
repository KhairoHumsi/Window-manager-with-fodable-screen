package com.khairo.windowmanagerwithfodablescreen.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khairo.windowmanagerwithfodablescreen.R
import com.khairo.windowmanagerwithfodablescreen.common.App
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.data.models.CategoriesModel
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel
import com.khairo.windowmanagerwithfodablescreen.data.repositories.MainRepository
import com.khairo.windowmanagerwithfodablescreen.data.utils.suspendToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val subTotal = ObservableField(0.0f)
    val discount = ObservableField(3f)
    val totalBill = ObservableField(0.0f)

    val main = MutableLiveData<List<CategoriesModel>>().apply {
        viewModelScope.launch(Dispatchers.IO) {
            postValue(repository.getCategories())
        }
    }

    val cart: MutableStateFlow<List<CartModel>> = MutableStateFlow(emptyList())

    val items = MutableLiveData<List<ItemModel>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            cart.emit(repository.getCart())
        }
    }

    fun getItems(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            items.postValue(repository.getItems(categoryId = categoryId))
        }
    }

    fun addItemToCart(model: ItemModel, added: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.checkFromItem(itemId = model.id).apply {
                if (!this) {
                    repository.addToCart(model = model) {
                        cart.emit(it)
                        withContext(Dispatchers.Main) {
                            added()
                        }
                    }
                } else
                    App.instance.getString(R.string.exist_warning).suspendToast(App.instance)

            }
            getBill()
        }
    }

    fun increaseItem(itemId: Int, updated: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.increaseItem(itemId = itemId) {
                cart.emit(it)
                getBill()
                withContext(Dispatchers.Main) {
                    updated()
                }
            }
        }
    }

    fun decreaseItem(itemId: Int, updated: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.decreaseItem(itemId = itemId) {
                cart.emit(it)
                getBill()
                withContext(Dispatchers.Main) {
                    updated()
                }
            }
        }
    }

    fun removeItem(item: CartModel, updated: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeItem(item = item) {
                cart.emit(it)
                getBill()
                withContext(Dispatchers.Main) {
                    updated()
                }
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearCart()
            cart.emit(ArrayList())
            getBill()
        }
    }

    private fun getBill() {
        viewModelScope.launch(Dispatchers.IO) {
            subTotal.set(repository.getSubTotalBill())
            val bill =
                if ((subTotal.get() ?: 0f) - (discount.get() ?: 0f) < 0) 0f else (subTotal.get()
                    ?: 0f) - (discount.get() ?: 0f)
            totalBill.set(bill)
        }
    }
}
