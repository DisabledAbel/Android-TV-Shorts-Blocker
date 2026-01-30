package com.example.shortsblocker

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    
    private lateinit var statusText: TextView
    private lateinit var toggleButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        statusText = findViewById(R.id.statusText)
        toggleButton = findViewById(R.id.toggleButton)
        
        toggleButton.setOnClickListener {
            openAccessibilitySettings()
        }
        
        findViewById<Button>(R.id.testButton).setOnClickListener {
            // Test if service is working
            if (isServiceEnabled()) {
                statusText.text = "✓ Service Active - Shorts will be blocked automatically"
                statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            } else {
                statusText.text = "✗ Service Disabled - Enable in Accessibility Settings"
                statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        updateStatus()
    }
    
    private fun updateStatus() {
        if (isServiceEnabled()) {
            statusText.text = "Status: ACTIVE\nShortsBlocker is running"
            toggleButton.text = "Accessibility Settings (Enabled)"
        } else {
            statusText.text = "Status: DISABLED\nEnable in Accessibility Settings to block Shorts"
            toggleButton.text = "Enable ShortsBlocker"
        }
    }
    
    private fun isServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        
        return enabledServices.contains("${packageName}/${packageName}.ShortsBlockerService")
    }
    
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}
