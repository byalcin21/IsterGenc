package com.zexly.istergenc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.zexly.istergenc.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null) {
            val intent = Intent(this, AnaSayfaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}