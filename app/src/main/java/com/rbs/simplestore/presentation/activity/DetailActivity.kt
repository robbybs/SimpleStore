package com.rbs.simplestore.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rbs.simplestore.R
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.databinding.ActivityDetailBinding
import com.rbs.simplestore.domain.mapper.toCart
import com.rbs.simplestore.domain.model.ProductDomain
import com.rbs.simplestore.presentation.viewmodel.DetailViewModel
import com.rbs.simplestore.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>()
    private var data: ProductDomain? = null
    private var userID = 0

    companion object {
        private const val DATA = "data"
        private const val ID = "id"

        @JvmStatic
        fun launch(context: Context, data: ProductDomain, userId: Int) {
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DATA, data)
                putExtra(ID, userId)
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
        data = intent.getParcelableExtra(DATA)
        userID = intent.getIntExtra(ID, 0)
    }

    private fun subscribeUI() {
        with(binding) {
            data?.apply {
                setImage(ivProduct, image)
                setText(tvName, title)
                setText(tvSubtitle, getString(R.string.text_subtitle_format, price, category))
                setText(tvDescription, description)
                addCart()
            }
        }
    }

    private fun setImage(imageView: ImageView, value: String) {
        Glide.with(this@DetailActivity)
            .load(value)
            .into(imageView)
    }

    private fun setText(textView: TextView, value: String) {
        textView.text = value
    }

    private fun addCart() {
        binding.btnAddCart.setOnClickListener {
            data?.let { product -> viewModel.addCart(product.toCart(userID)) }
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            viewModel.viewState.collectLatest { state ->
                when (state) {
                    ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> setSuccess()
                    is ResultState.Error -> setError()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun setSuccess() {
        showLoading(false)
        showToast(getString(R.string.text_success_add_to_cart))
        finish()
    }

    private fun setError() {
        showLoading(false)
        showToast(getString(R.string.text_failed_add_to_cart))
    }
}