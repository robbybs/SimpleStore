package com.rbs.simplestore.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.rbs.simplestore.R
import com.rbs.simplestore.databinding.ActivityRegisterBinding
import com.rbs.simplestore.presentation.viewmodel.RegisterViewModel
import com.rbs.simplestore.utils.hideKeyboard
import com.rbs.simplestore.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel>()

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            Intent(context, RegisterActivity::class.java).apply {
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
        setupFullName()
        setupUsername()
        setupEmail()
        setupPassword()
        doRegister()
    }

    private fun setupFullName() {
        binding.etFullname.addTextChangedListener {
            viewModel.apply {
                fullname = it.toString()
                validate()
            }
        }
    }

    private fun setupUsername() {
        binding.etUsername.addTextChangedListener {
            viewModel.apply {
                username = it.toString()
                validate()
            }
        }
    }

    private fun setupEmail() {
        binding.etEmail.apply {
            addTextChangedListener {
                viewModel.apply {
                    val text = it.toString()
                    if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        error = context.getString(R.string.text_invalid_email)
                    } else {
                        email = text
                        validate()
                        error = null
                    }
                }
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

    private fun doRegister() {
        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            viewModel.registerUser()
        }
    }

    private fun subscribeObserver() {
        lifecycleScope.launch {
            viewModel.isButtonEnabled.collect {
                binding.btnRegister.apply {
                    isEnabled = it
                }
            }
        }

        lifecycleScope.launch {
            viewModel.registerEvent.collectLatest { isSuccess ->
                if (isSuccess) setSuccessRegister()
            }
        }
    }

    private fun setSuccessRegister() {
        showToast(getString(R.string.text_success_register))
        MainActivity.launch(this@RegisterActivity)
        finishAffinity()
    }
}