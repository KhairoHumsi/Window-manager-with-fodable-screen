package com.khairo.windowmanagerwithfodablescreen.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.khairo.windowmanagerwithfodablescreen.R
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.data.models.CategoriesModel
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel
import com.khairo.windowmanagerwithfodablescreen.data.utils.MarginDecoration
import com.khairo.windowmanagerwithfodablescreen.data.utils.getFeatureBoundsInWindow
import com.khairo.windowmanagerwithfodablescreen.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ItemsAdapter.OnClickListener,
    CategoriesAdapter.OnClickListener, CartAdapter.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var windowInfoTracker: WindowInfoTracker
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        windowInfoTracker = WindowInfoTracker.getOrCreate(this@MainActivity)

        onWindowLayoutInfoChange()

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

    private fun onWindowLayoutInfoChange() {
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                windowInfoTracker.windowLayoutInfo(this@MainActivity)
                    .collect { value ->
                        updateUI(value)
                    }
            }
        }
    }

    private fun updateUI(newLayoutInfo: WindowLayoutInfo) {
        if (newLayoutInfo.displayFeatures.isNotEmpty())
            alignViewToFoldingFeatureBounds(newLayoutInfo)
    }

    private fun alignViewToFoldingFeatureBounds(newLayoutInfo: WindowLayoutInfo) {
        val constraintLayout = binding.mainView
        val set = ConstraintSet()
        set.clone(constraintLayout)

        val foldingFeature = newLayoutInfo.displayFeatures[0] as FoldingFeature
        val bounds = binding.root.getFeatureBoundsInWindow(foldingFeature)

        bounds?.let { rect ->
            val horizontalFoldingFeatureHeight = (rect.bottom - rect.top).coerceAtLeast(1)
            val verticalFoldingFeatureWidth = (rect.right - rect.left).coerceAtLeast(1)

            set.constrainHeight(
                R.id.folding_feature,
                horizontalFoldingFeatureHeight
            )
            set.constrainWidth(
                R.id.folding_feature,
                verticalFoldingFeatureWidth
            )

            set.connect(
                R.id.folding_feature, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 0
            )
            set.connect(
                R.id.folding_feature, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0
            )

            if (foldingFeature.orientation == FoldingFeature.Orientation.VERTICAL) {
                set.setMargin(R.id.folding_feature, ConstraintSet.START, rect.left)
//                set.connect(
//                    R.id.layout_change, ConstraintSet.END,
//                    R.id.folding_feature, ConstraintSet.START, 0
//                )
            } else {
                // FoldingFeature is Horizontal
                set.setMargin(R.id.folding_feature, ConstraintSet.TOP, rect.top)
//                set.connect(
//                    R.id.layout_change, ConstraintSet.TOP,
//                    R.id.folding_feature, ConstraintSet.BOTTOM, 0
//                )
            }

            // Set the view to visible and apply constraints
            set.setVisibility(R.id.folding_feature, View.VISIBLE)
            set.applyTo(constraintLayout)
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
