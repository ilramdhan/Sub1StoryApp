package com.dicoding.sub1storyapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1storyapp.MainActivity
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.data.remote.ApiResponse
import com.dicoding.sub1storyapp.data.remote.authpack.Body
import com.dicoding.sub1storyapp.databinding.ActivityLoginBinding
import com.dicoding.sub1storyapp.registration.RegistrationActivity
import com.dicoding.sub1storyapp.utils.Session
import com.dicoding.sub1storyapp.utils.ValManager.KEY_EMAIL
import com.dicoding.sub1storyapp.utils.ValManager.KEY_ISLOGIN
import com.dicoding.sub1storyapp.utils.ValManager.KEY_TOKEN
import com.dicoding.sub1storyapp.utils.ValManager.KEY_USER_ID
import com.dicoding.sub1storyapp.utils.ValManager.KEY_USER_NAME
import com.dicoding.sub1storyapp.utils.extension.gone
import com.dicoding.sub1storyapp.utils.extension.show
import com.dicoding.sub1storyapp.utils.extension.showOKDialog
import com.dicoding.sub1storyapp.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!
    private lateinit var pref: Session

    companion object {
        fun begin(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_activityLoginBinding?.root)

        pref = Session(this)

        initAct()
    }

    private fun initAct() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                email.isBlank() -> {
                    binding.etEmail.requestFocus()
                    binding.etEmail.error = getString(string.email_empty)
                }
                password.isBlank() -> {
                    binding.etPassword.requestFocus()
                    binding.etPassword.error = getString(string.pass_empty)
                }
                else -> {
                    val request = Body(email, password)
                    loginUser(request, email)
                }
            }
        }
        binding.tvRegisLink.setOnClickListener {
            RegistrationActivity.begin(this)
        }
    }

    private fun loginUser(loginBody: Body, email: String) {
        loginViewModel.userLogin(loginBody).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    isLoading(true)
                }
                is ApiResponse.Success -> {
                    try {
                        isLoading(false)
                        val userRespon = response.data.loginResult
                        pref.apply {
                            setStringPreference(KEY_USER_ID, userRespon.userId)
                            setStringPreference(KEY_TOKEN, userRespon.token)
                            setStringPreference(KEY_USER_NAME, userRespon.name)
                            setStringPreference(KEY_EMAIL, email)
                            setBooleanPreference(KEY_ISLOGIN, true)
                        }
                    } finally {
                        MainActivity.begin(this)
                    }
                }
                is ApiResponse.Error -> {
                    isLoading(false)
                    showOKDialog(getString(string.dialog_error), getString(string.userpass_error))
                }
                else -> {
                    showToast(getString(string.statement_error))
                }
            }
        }
    }
    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.etEmail.isClickable = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isClickable = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }
}