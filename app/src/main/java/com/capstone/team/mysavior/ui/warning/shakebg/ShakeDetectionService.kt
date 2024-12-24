package com.capstone.team.mysavior.ui.warning.shakebg

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.capstone.team.mysavior.R
import com.capstone.team.mysavior.ui.warning.WarningActivity
import kotlin.math.sqrt

class ShakeDetectionService : Service() {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorEventListener: SensorEventListener
    private var lastAcceleration = 0f
    private var currentAcceleration = 0f
    private var acceleration = 0f

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification: Notification = createNotification()
        startForeground(1, notification)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    lastAcceleration = currentAcceleration
                    currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val delta: Float = currentAcceleration - lastAcceleration
                    acceleration = acceleration * 0.1f + delta

                    if (acceleration > 30) {
                        showWarningActivity()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ShakeDetectionChannel",
                "Shake Detection Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "ShakeDetectionChannel")
            .setContentTitle("Shake Detection Active")
            .setContentText("Monitoring device shake in the background.")
            .setSmallIcon(R.drawable.ic_warning_24)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun showWarningActivity() {
        val intent = Intent(this, WarningActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
