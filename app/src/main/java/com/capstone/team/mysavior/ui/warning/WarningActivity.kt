package com.capstone.team.mysavior.ui.warning

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.team.mysavior.ui.MainActivity
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.databinding.ActivityWarningBinding
import java.util.Locale

class WarningActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityWarningBinding
    private lateinit var tts: TextToSpeech
    private val phoneNumber = "+6287772370386"
    private val message = "Memulai Panggilan Telpon!"
    private val autoCallDelay: Long = 5000
    private val countdownTime: Long = 5000

    private val callPermissionCode = 1

    private lateinit var countdownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()

        tts = TextToSpeech(this, this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE), callPermissionCode)
        }

        // Setup countdown timer
        setupCountdownTimer()

        // Start the countdown
        countdownTimer.start()
    }

    private fun setupAction() {
        binding.root.findViewById<Button>(R.id.fineButton).setOnClickListener {
            Toast.makeText(this, "Warning Closed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupCountdownTimer() {
        // Find the TextView for displaying the countdown
        val countdownTextView: TextView = findViewById(R.id.countdownTextView)

        countdownTimer = object : CountDownTimer(countdownTime, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Update the TextView with the remaining time
                countdownTextView.text = "Waktu tersisa: ${millisUntilFinished / 1000} detik"
            }

            override fun onFinish() {
                // Start phone call and TTS once the countdown finishes
                makePhoneCall()
                speakMessage(message)
            }
        }
    }

    private fun makePhoneCall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        }
    }

    private fun speakMessage(message: String) {
        if (tts.isSpeaking) {
            tts.stop() // Berhenti berbicara jika ada teks yang sedang dibacakan
        }
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val langResult = tts.setLanguage(Locale("id", "ID"))
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Bahasa tidak didukung", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "TTS gagal diinisialisasi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == callPermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Izin diberikan, sekarang Anda dapat melakukan panggilan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin panggilan ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }

    private fun playAnimation() {
        val waveAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.imageViewWarning, View.SCALE_X, 1f, 1.2f, 1f).apply {
                    duration = 2000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                },
                ObjectAnimator.ofFloat(binding.imageViewWarning, View.SCALE_Y, 1f, 1.2f, 1f).apply {
                    duration = 2000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                },
                ObjectAnimator.ofFloat(binding.imageViewWarning, View.ALPHA, 0.8f, 1f, 0.8f).apply {
                    duration = 2000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                }
            )
        }
        waveAnimator.start()
    }
}