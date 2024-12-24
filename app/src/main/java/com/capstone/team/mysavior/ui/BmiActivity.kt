package com.capstone.team.mysavior.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.team.mysavior.databinding.ActivityBmiactivityBinding

class BmiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan binding untuk menghubungkan layout
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur batas minimum dan maksimum pada NumberPicker
        binding.weightPicker.minValue = 30
        binding.weightPicker.maxValue = 150

        binding.heightPicker.minValue = 100
        binding.heightPicker.maxValue = 250

        // Listener untuk menghitung BMI setiap kali nilai berubah
        binding.weightPicker.setOnValueChangedListener { _, _, _ -> calculateBMI() }
        binding.heightPicker.setOnValueChangedListener { _, _, _ -> calculateBMI() }
    }

    // Fungsi untuk menghitung BMI
    private fun calculateBMI() {
        val height = binding.heightPicker.value
        val doubleHeight = height.toDouble() / 100

        val weight = binding.weightPicker.value

        val bmi = weight.toDouble() / (doubleHeight * doubleHeight)

        binding.resultsTV.text = String.format("Your BMI is: %.2f", bmi)
        binding.healthyTV.text = String.format("Considered: %s", healthyMessage(bmi))
    }

    // Fungsi untuk memberikan pesan kategori kesehatan berdasa rkan BMI
    private fun healthyMessage(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Healthy"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }
}