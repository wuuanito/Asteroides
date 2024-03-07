package com.example.asteroides
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
class PreferenciasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(android.R.id.content,PreferenciasFragment()).commit()
    }
}