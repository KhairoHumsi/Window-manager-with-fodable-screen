package com.khairo.windowmanagerwithfodablescreen.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.data.models.CategoriesModel
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel
import com.khairo.windowmanagerwithfodablescreen.data.utils.MarginDecoration
import com.khairo.windowmanagerwithfodablescreen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ItemsAdapter.OnClickListener,
    CategoriesAdapter.OnClickListener, CartAdapter.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = mainViewModel
            initAdapters()

            mainViewModel.run {
                main.observe(this@MainActivity) { categoriesAdapter.submitList(it) }
                items.observe(this@MainActivity) { itemsAdapter.submitList(it) }

                lifecycleScope.launch(Dispatchers.Main) {
                    cart.collect { cartAdapter.submitList(it) }
                }
            }

            clearCart.setOnClickListener { clearAllCart() }

            pay.setOnClickListener { clearAllCart() }
        }
    }

    private fun clearAllCart() {
        mainViewModel.clearCart()
        cartAdapter.submitList(null)
    }

    private fun ActivityMainBinding.initAdapters() {
        mainViewModel.apply {
            categoriesAdapter = CategoriesAdapter(this@MainActivity)
            categoriesRecycler.adapter = categoriesAdapter

            itemsAdapter = ItemsAdapter(this@MainActivity)
            itemRecycler.adapter = itemsAdapter
            itemRecycler.addItemDecoration(MarginDecoration(this@MainActivity))
            itemRecycler.setHasFixedSize(true)

            cartAdapter = CartAdapter(this@MainActivity)
            cartRecycler.adapter = cartAdapter
        }
    }

    override fun addItemToCart(model: ItemModel) {
        mainViewModel.addItemToCart(model = model) {
            cartAdapter.notifyItemInserted(cartAdapter.currentList.size)
        }
    }

    override fun loadItems(model: CategoriesModel) {
        mainViewModel.getItems(model.id)
    }

    override fun removeItem(model: CartModel, position: Int) {
        mainViewModel.removeItem(model) {
            cartAdapter.notifyItemRemoved(position)
        }
    }

    override fun decreaseItem(itemId: Int, position: Int) {
        mainViewModel.decreaseItem(itemId) {
            cartAdapter.notifyItemChanged(position)
        }
    }

    override fun increaseItem(itemId: Int, position: Int) {
        mainViewModel.increaseItem(itemId) {
            cartAdapter.notifyItemChanged(position)
        }
    }
}
