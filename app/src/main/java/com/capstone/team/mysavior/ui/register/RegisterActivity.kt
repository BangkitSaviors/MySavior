package com.capstone.team.mysavior.ui.register

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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.capstone.team.mysavior.data.pref.UserModel
import com.capstone.team.mysavior.data.pref.UserPreference
import com.capstone.team.mysavior.data.pref.dataStore
import com.capstone.team.mysavior.data.remote.request.RegisterRequest
import com.capstone.team.mysavior.databinding.ActivityRegisterBinding
import com.capstone.team.mysavior.ui.ViewModelFactory
import com.capstone.team.mysavior.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        binding.passwordEditText.addTextChangedListener { text ->
            binding.passwordEditText.error = if (text.isNullOrEmpty() || text.length < 8) {
                "Password tidak boleh kurang dari 8 karakter"
            } else {
                null
            }
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "field tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(this, "Password harus lebih dari 8 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            binding.signupButton.isEnabled = false

            registerViewModel.registerUser(RegisterRequest(name, email, password))
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    binding.progressBar.visibility = View.GONE
//                    binding.signupButton.isEnabled = true
//
//                    if (task.isSuccessful) {
//                        val registerRequest = RegisterRequest(name, email, password)
//                        registerViewModel.registerUser(registerRequest)
//                    } else {
//                        val error = task.exception?.message ?: "Pendaftaran gagal."
//                        if (error.contains("already exists", true)) {
//                            showError("Email sudah terdaftar. Silakan login atau gunakan email lain.")
//                        } else {
//                            showError(error)
//                        }
//                    }
//                }

        }
        binding.signInLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerResult.collect { result ->
                result?.onSuccess {
                    showSuccessDialog(it.message.toString())
                }?.onFailure {
                    val errorMessage = it.message ?: "Pendaftaran gagal."
                    if (errorMessage.contains("already registered")) {
                        showError("Email sudah terdaftar. Silakan gunakan email lain.")
                    } else {
                        showError(errorMessage)
                    }
                }
            }
        }
    }

    private fun saveUserSession(user: UserModel) {
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(applicationContext.dataStore)
            userPreference.saveSession(user)
        }
    }

    private fun showSuccessDialog(name: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Akun Berhasil Dibuat")
            setMessage("Selamat, $name berhasil terdaftar. Silakan login.")
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