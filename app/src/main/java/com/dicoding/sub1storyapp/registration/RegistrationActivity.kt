package com.dicoding.sub1storyapp.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.authpack.AuthBody
import com.dicoding.sub1storyapp.databinding.ActivityRegistrationBinding
import com.dicoding.sub1storyapp.login.LoginActivity
import com.dicoding.sub1storyapp.utils.UiValue
import com.dicoding.sub1storyapp.utils.extension.isEmailValid
import com.dicoding.sub1storyapp.utils.extension.showOKDialog
import com.dicoding.sub1storyapp.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registViewModel: RegistrationViewModel by viewModels()
    private var _activityRegistBinding: ActivityRegistrationBinding? = null
    private val binding get() = _activityRegistBinding!!

    companion object {
        fun begin(context: Context) {
            val intent = Intent(context, RegistrationActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRegistBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(_activityRegistBinding?.root)

        initAct()
    }

    private fun initAct() {
        binding.btnRegist.setOnClickListener {
            val name = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    name.isBlank() -> binding.etUsername.error = getString(string.name_empty)
                    email.isBlank() -> binding.etEmail.error = getString(string.email_empty)
                    !email.isEmailValid() -> binding.etEmail.error = getString(string.email_error)
                    password.isBlank() -> binding.etPassword.error = getString(string.pass_empty)
                    else -> {
                        val request = AuthBody(
                            name, email, password
                        )
                        registUser(request)
                    }
                }
            }, UiValue.DELAYED)
        }
        binding.tvLoginLink.setOnClickListener {
            LoginActivity.begin(this)
        }
    }

    private fun registUser(newUser: AuthBody) {
        registViewModel.registUser(newUser).observe(this) { response ->
            when(response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    try {
                        showLoading(false)
                    } finally {
                        LoginActivity.begin(this)
                        finish()
                        showToast(getString(string.regist_succsess))
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    showOKDialog(getString(string.dialog_error), response.errorMessage)
                }
                else -> {
                    showToast(getString(string.statement_error))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.etUsername.isClickable = !isLoading
        binding.etUsername.isEnabled = !isLoading
        binding.etEmail.isClickable = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isClickable = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.btnRegist.isClickable = !isLoading
    }
}