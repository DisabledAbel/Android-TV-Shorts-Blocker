package com.example.shortsblocker

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class ShortsBlockerService : AccessibilityService() {
    
    private val TAG = "ShortsBlockerTV"
    private val handler = Handler(Looper.getMainLooper())
    private var lastBlockTime = 0L
    private val BLOCK_COOLDOWN = 2000L // Prevent multiple triggers
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Service connected")
        showToast("ShortsBlocker Active")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        
        // Check both YouTube TV and regular YouTube (if sideloaded)
        if (packageName !in listOf("com.google.android.youtube.tv", "com.google.android.youtube")) {
            return
        }
        
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                checkAndBlockShorts()
            }
        }
    }
    
    private fun checkAndBlockShorts() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBlockTime < BLOCK_COOLDOWN) return
        
        val rootNode = rootInActiveWindow ?: return
        
        try {
            // Multiple detection methods for different YouTube TV versions
            if (isShortsPlayerDetected(rootNode)) {
                Log.d(TAG, "Shorts detected! Blocking...")
                blockShorts()
                lastBlockTime = currentTime
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking nodes: ${e.message}")
        } finally {
            rootNode.recycle()
        }
    }
    
    private fun isShortsPlayerDetected(rootNode: AccessibilityNodeInfo): Boolean {
        // Method 1: Check for specific Shorts UI elements
        val shortsIndicators = listOf(
            "shorts",
            "Reel",
            "shorts_player",
            "shorts_container"
        )
        
        // Method 2: Check window title/class
        val className = rootNode.className?.toString()?.lowercase() ?: ""
        if (className.contains("reel") || className.contains("short")) {
            return true
        }
        
        // Method 3: Traverse tree for content descriptions indicating Shorts
        val nodeQueue = ArrayDeque<AccessibilityNodeInfo>()
        nodeQueue.add(rootNode)
        var depth = 0
        val maxDepth = 10 // Prevent infinite loops
        
        while (nodeQueue.isNotEmpty() && depth < maxDepth) {
            val node = nodeQueue.removeFirst()
            
            // Check content description
            val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
            if (contentDesc.contains("short") && (contentDesc.contains("video") || contentDesc.contains("player"))) {
                return true
            }
            
            // Check view ID resource name
            val viewId = node.viewIdResourceName?.lowercase() ?: ""
            if (viewId.contains("short") || viewId.contains("reel")) {
                return true
            }
            
            // Check text (some TV versions show "Shorts" text)
            val text = node.text?.toString()?.lowercase() ?: ""
            if (text == "shorts" && node.className?.contains("TextView") == true) {
                // Additional check: is this in a player context?
                if (viewId.contains("title") || viewId.contains("header")) {
                    return true
                }
            }
            
            // Add children to queue
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { nodeQueue.add(it) }
            }
            depth++
        }
        
        return false
    }
    
    private fun blockShorts() {
        // Method 1: Press back button to exit Shorts
        performGlobalAction(GLOBAL_ACTION_BACK)
        
        // Method 2: If back doesn't work, try to navigate to Home/Subscriptions
        handler.postDelayed({
            val rootNode = rootInActiveWindow
            if (rootNode != null && isShortsPlayerDetected(rootNode)) {
                // Still in Shorts, try pressing back again or navigate home
                performGlobalAction(GLOBAL_ACTION_HOME)
            }
            rootNode?.recycle()
        }, 500)
        
        showToast("Shorts Blocked")
    }
    
    private fun showToast(message: String) {
        handler.post {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }
    
    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Optional: Intercept d-pad center button on Shorts thumbnails to prevent entry
        return super.onKeyEvent(event)
    }
}
