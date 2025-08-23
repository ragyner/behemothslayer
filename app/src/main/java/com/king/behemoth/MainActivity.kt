package com.king.behemoth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Load our XML layout (not a programmatic TextView)
        setContentView(R.layout.activity_main)
    }
}
