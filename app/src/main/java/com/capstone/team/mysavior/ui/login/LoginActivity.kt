package com.capstone.team.mysavior.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.team.mysavior.data.remote.request.LoginRequest
import com.capstone.team.mysavior.ui.MainActivity
import com.capstone.team.mysavior.databinding.ActivityLoginBinding
import com.capstone.team.mysavior.ui.ViewModelFactory
import com.capstone.team.mysavior.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupView()
        setupAction()
        observeViewModel()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        binding.progressBar.visibility = View.GONE
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true

                    if (task.isSuccessful) {
                        val loginRequest = LoginRequest(email, password)
                        viewModel.login(loginRequest)
                        showSuccessDialog(email)
                    } else {
                        val error = task.exception?.message ?: "Login gagal."
                        showError(error)
                    }
                }
            }
            binding.signInLink.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                binding.progressBar.visibility = View.GONE
                binding.loginButton.isEnabled = true

                result?.onSuccess {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }?.onFailure {
                    showError(it.message ?: "Login gagal.")
                }
            }
        }
    }

    private fun showSuccessDialog(email: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Berhasil")
            setMessage("Selamat Datang, $email.")
            setPositiveButton("Oke") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}