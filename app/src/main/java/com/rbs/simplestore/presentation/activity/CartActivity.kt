package com.rbs.simplestore.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.rbs.simplestore.R
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.databinding.ActivityCartBinding
import com.rbs.simplestore.domain.model.CartDomain
import com.rbs.simplestore.presentation.adapter.CartAdapter
import com.rbs.simplestore.presentation.viewmodel.CartViewModel
import com.rbs.simplestore.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private val binding: ActivityCartBinding by lazy { ActivityCartBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CartViewModel>()
    private val cartAdapter by lazy { CartAdapter() }
    private var itemRemoved: CartDomain? = null

    companion object {
        private const val ID = "id"

        @JvmStatic
        fun launch(context: Context, userID: Int) {
            Intent(context, CartActivity::class.java).apply {
                putExtra(ID, userID)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getIntentData()
        subscribeUI()
        subscribeObserver()
    }

    private fun getIntentData() {
        val id = intent.getIntExtra(ID, 0)
        viewModel.getCarts(id)
    }

    private fun subscribeUI() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvCart.adapter = cartAdapter
        cartAdapter.apply {
            setOnDeleteClickListener { data ->
                itemRemoved = data
                viewModel.deleteCart(data.id)
            }
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                when (state) {
                    ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> setData(state.data)
                    is ResultState.Error -> showLoading(false)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteEvent.collect { state ->
                when (state) {
                    ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> setSuccessDelete()
                    is ResultState.Error -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun setData(data: List<CartDomain>) {
        showLoading(false)
        showEmptyData(data.isEmpty())
        cartAdapter.submitList(data)
    }

    private fun setSuccessDelete() {
        showLoading(false)
        cartAdapter.apply {
            val currentList = currentList.toMutableList()
            currentList.remove(itemRemoved)
            submitList(currentList)
            showEmptyData(currentList.isEmpty())
        }
        showToast(getString(R.string.text_success_deleted_item))
    }

    private fun showEmptyData(state: Boolean) {
        binding.tvEmptyCart.isVisible = state
    }
}