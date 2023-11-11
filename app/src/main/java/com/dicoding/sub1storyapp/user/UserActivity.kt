package com.dicoding.sub1storyapp.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.sub1storyapp.R.string
import com.dicoding.sub1storyapp.databinding.ActivityUserBinding
import com.dicoding.sub1storyapp.login.LoginActivity
import com.dicoding.sub1storyapp.utils.Session

@Suppress("DEPRECATION")
class UserActivity : AppCompatActivity() {

    private var _activityUserBinding: ActivityUserBinding? = null
    private val binding get() = _activityUserBinding!!
    private lateinit var pref: Session

    companion object {
        fun begin(context: Context) {
            val intent = Intent(context, UserActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityUserBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(_activityUserBinding?.root)
        pref = Session(this)
        initUI()
        initAct()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(string.profile)
        binding.tvName.text = pref.getUserName
        binding.tvEmail.text = pref.getEmail
    }

    private fun initAct() {
        binding.btnLogout.setOnClickListener {
            logoutDialog()
        }
    }

    private fun logoutDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(string.logout_confirm))
            ?.setPositiveButton(getString(string.logout_yes)) { _, _ ->
                pref.clearPreferences()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            ?.setNegativeButton(getString(string.logout_no), null)
        val alert = alertDialog.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}