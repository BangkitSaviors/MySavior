package com.capstone.team.mysavior.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.databinding.ActivitySettingBinding
import com.capstone.team.mysavior.data.pref.UserModel
import com.capstone.team.mysavior.data.UserRepository
import com.capstone.team.mysavior.data.pref.UserPreference
import com.capstone.team.mysavior.data.pref.dataStore
import com.capstone.team.mysavior.data.remote.retrofit.ApiConfig
import com.capstone.team.mysavior.ui.ViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var userRepository: UserRepository
    private lateinit var settingViewModel: SettingViewModel

    private var profileImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(applicationContext.dataStore)
        val token = ""
        val apiService = ApiConfig.getApiService()
        userRepository = UserRepository.getInstance(userPreference, apiService)
        settingViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[SettingViewModel::class.java]

        setupUI()
        setupActions()
    }

    private fun setupUI() {
        settingViewModel.getSession().observe(this) { user ->
            binding.profileName.text = user.name
            binding.profileBirthdate.setText(user.birthDate)
            binding.profilePhoneNumber.setText(user.phoneNumber)

            profileImageUri = user.profilePicture
            profileImageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(binding.profileImage)
            } ?: run {
                binding.profileImage.setImageResource(R.drawable.ic_person_black_24dp)
            }
        }
    }

    private fun setupActions() {
        binding.changeProfilePictureButton.setOnClickListener {
            // Menangani pemilihan gambar untuk foto profil
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.profileName.text.toString()
            val birthdate = binding.profileBirthdate.text.toString()
            val phoneNumber = binding.profilePhoneNumber.text.toString()

            val user = UserModel(
                name = name,
                email = "",
                token = "",
                isLogin = true,
                birthDate = birthdate,
                phoneNumber = phoneNumber,
                profilePicture = profileImageUri
            )

            settingViewModel.saveSession(user) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // Menggunakan ActivityResultContracts untuk memilih gambar
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            profileImageUri = data?.data.toString() // Simpan URI foto profil

            // Tampilkan foto profil yang dipilih
            Glide.with(this) // Gunakan Glide untuk memuat gambar
                .load(profileImageUri) // Muat gambar menggunakan URI
                .into(binding.profileImage) // Misalnya, menggunakan ImageView untuk foto profil
        }
    }
}
