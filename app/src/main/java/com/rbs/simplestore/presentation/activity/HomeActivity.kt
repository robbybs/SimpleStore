package com.rbs.simplestore.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.rbs.simplestore.R
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.databinding.ActivityHomeBinding
import com.rbs.simplestore.domain.model.ProductDomain
import com.rbs.simplestore.domain.model.UserDomain
import com.rbs.simplestore.presentation.adapter.ProductAdapter
import com.rbs.simplestore.presentation.bottomsheet.ProfileBottomSheet
import com.rbs.simplestore.presentation.viewmodel.HomeViewModel
import com.rbs.simplestore.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HomeViewModel>()
    private val productAdapter by lazy { ProductAdapter() }
    private var listProduct = listOf<ProductDomain>()
    private var userId = 0

    private var currentChip: Chip? = null
    private val activeColor = com.google.android.material.R.color.material_dynamic_tertiary30
    private val inactiveColor = com.google.android.material.R.color.material_dynamic_tertiary70
    private val textActiveColor = R.color.white
    private val textInactiveColor = com.google.android.material.R.color.material_dynamic_tertiary10

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            Intent(context, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        subscribeUI()
        subscribeObserver()
    }

    private fun subscribeUI() {
        initRecyclerView()
        openProfile()
        retryFetch()
        showCart()
    }

    private fun initRecyclerView() {
        binding.rvProduct.adapter = productAdapter
        productAdapter.setOnItemClickListener {
            DetailActivity.launch(this, it, userId)
        }
    }

    private fun retryFetch() {
        binding.btnRetry.setOnClickListener { viewModel.getProducts() }
    }

    private fun showCart() {
        binding.btnCart.setOnClickListener {
            CartActivity.launch(this, userId)
        }
    }

    private fun openProfile() {
        binding.ivProfile.setOnClickListener {
            viewModel.getUser(userId)
        }
    }

    private fun subscribeObserver() {
        observeID()
        observeProducts()
        observeUsers()
        observeLogout()
    }

    private fun observeID() {
        lifecycleScope.launch {
            viewModel.userEvent.collect {
                userId = it
                openProfile()
            }
        }
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            viewModel.productState.collect { state ->
                when (state) {
                    ResultState.Loading -> {
                        showLoadingLayout(true)
                        showErrorLayout(false)
                    }

                    is ResultState.Success -> {
                        showLoadingLayout(false)
                        setListProduct(state.data)
                    }

                    is ResultState.Error -> {
                        showLoadingLayout(false)
                        showErrorLayout(true)
                    }
                }
            }
        }
    }

    private fun observeUsers() {
        lifecycleScope.launch {
            viewModel.userState.collect { state ->
                state.apply {
                    when (this) {
                        ResultState.Loading -> showLoadingUser(true)
                        is ResultState.Success -> {
                            showLoadingUser(false)
                            setUserData(data)
                        }

                        is ResultState.Error -> {
                            showLoadingUser(false)
                            showToast(message)
                        }
                    }
                }
            }
        }
    }

    private fun observeLogout() {
        lifecycleScope.launch {
            viewModel.logoutEvent.collectLatest { isLogout ->
                if (isLogout) backToLogin()
            }
        }
    }

    private fun setListProduct(data: List<ProductDomain>) {
        listProduct = data
        setupCategory()
    }

    private fun showLoadingLayout(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun showLoadingUser(state: Boolean) {
        binding.progressBarUser.isVisible = state
    }

    private fun showErrorLayout(state: Boolean) {
        binding.llError.isVisible = state
    }

    private fun backToLogin() {
        MainActivity.launch(this@HomeActivity)
        finishAffinity()
    }

    private fun setupCategory() {
        binding.chipProduct.apply {
            removeAllViews()
            val category = listProduct.map { it.category }.distinct()
            for (item in category) {
                val chip = Chip(this@HomeActivity).apply {
                    text = item
                    isClickable = true
                    isCheckable = true

                    setChipTextColor(this, textInactiveColor)
                    setChipColor(this, inactiveColor)
                }

                addView(chip)
            }

            setOnCheckedStateChangeListener { group, checkedId ->
                currentChip?.let {
                    setChipTextColor(it, textInactiveColor)
                    setChipColor(it, inactiveColor)
                }

                if (checkedId.isNotEmpty()) {
                    val selectedChip = group.findViewById<Chip>(checkedId[0])
                    val selectedCategory = selectedChip.text.toString()
                    val filteredData = listProduct.filter { it.category == selectedCategory }

                    setData(filteredData)
                    setChipTextColor(selectedChip, textActiveColor)
                    setChipColor(selectedChip, activeColor)
                    currentChip = selectedChip
                } else {
                    setData(listProduct)
                    currentChip = null
                }
            }

            productAdapter.submitList(listProduct)
        }
    }

    private fun setChipColor(chip: Chip, color: Int) {
        chip.setChipBackgroundColorResource(color)
    }

    private fun setChipTextColor(chip: Chip, color: Int) {
        chip.setTextColor(ContextCompat.getColor(this, color))
    }

    private fun setData(data: List<ProductDomain>) {
        productAdapter.submitList(data) {
            binding.rvProduct.smoothScrollToPosition(0)
        }
    }

    private fun setUserData(data: UserDomain) {
        ProfileBottomSheet.showDialog(supportFragmentManager, data).apply {
            setOnLogout { viewModel.logoutUser() }
        }
    }
}