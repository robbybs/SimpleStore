package com.rbs.simplestore.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.rbs.simplestore.R
import com.rbs.simplestore.data.remote.model.LoginRequest
import com.rbs.simplestore.data.remote.network.ResultState
import com.rbs.simplestore.databinding.ActivityMainBinding
import com.rbs.simplestore.presentation.viewmodel.LoginViewModel
import com.rbs.simplestore.utils.hideKeyboard
import com.rbs.simplestore.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeUI()
        subscribeObserver()
    }

    private fun subscribeUI() {
        setupUsername()
        setupPassword()
        registerUser()
        doLogin()
    }

    private fun subscribeObserver() {
        isLoginObserver()
        buttonObserver()
        userLoginObserver()
    }

    private fun isLoginObserver() {
        lifecycleScope.launch {
            viewModel.loginEvent.collectLatest { isLogin ->
                if (isLogin) {
                    redirectToHome()
                } else {
                    setContentView(binding.root)
                }
            }
        }
    }

    private fun buttonObserver() {
        lifecycleScope.launch {
            viewModel.isButtonEnabled.collect { state ->
                binding.btnLogin.apply {
                    isEnabled = state
                }
            }
        }
    }

    private fun userLoginObserver() {
        lifecycleScope.launch {
            viewModel.viewState.collectLatest { state ->
                when (state) {
                    ResultState.Loading -> setLoading(true)
                    is ResultState.Success -> setSuccessLogin()
                    is ResultState.Error -> setFailedLogin(state.message)
                }
            }
        }
    }

    private fun setLoading(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    private fun redirectToHome() {
        HomeActivity.launch(this@MainActivity)
        finish()
    }

    private fun setSuccessLogin() {
        setLoading(false)
        redirectToHome()
    }

    private fun setFailedLogin(message: String) {
        setLoading(false)
        showToast(message)
    }

    private fun setupUsername() {
        binding.etUsername.addTextChangedListener {
            viewModel.apply {
                username = it.toString()
                validate()
            }
        }
    }

    private fun setupPassword() {
        binding.etPassword.addTextChangedListener {
            viewModel.apply {
                password = it.toString()
                validate()
            }
        }
    }

    private fun registerUser() {
        binding.tvRegister.setOnClickListener { RegisterActivity.launch(this)}
    }

    private fun doLogin() {
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            viewModel.loginUser()
        }
    }
}