package com.zexly.istergenc.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.zexly.istergenc.R

class MyProfilActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profil)
        auth= FirebaseAuth.getInstance()
        menu()
    }

    private val navigasZexly = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {

            R.id.paylasimYap -> {
                val intent= Intent(this, PaylasimYapActivity::class.java)
                startActivity(intent)

            }
            R.id.cikis -> {
                auth.signOut()
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.home->{
                val intent= Intent(this,AnaSayfaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        false
    }

    private fun menu(){
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNavigation.setOnItemSelectedListener(navigasZexly)
    }
}