package com.zexly.istergenc.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zexly.istergenc.R
import com.zexly.istergenc.adapter.AnaMenuRecyclerAdapter
import com.zexly.istergenc.model.Post
import kotlinx.android.synthetic.main.activity_ana_sayfa.*

class AnaSayfaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    var postListesi=ArrayList<Post>()
    private lateinit var recyclerViewAdapter: AnaMenuRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_sayfa)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        menu()
        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter= AnaMenuRecyclerAdapter(postListesi)
        recyclerView.adapter=recyclerViewAdapter
        verileriAl()
    }

    fun verileriAl(){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Yükleniyor,lütfen bekleyiniz...")
        progressDialog.show()

        database.collection("Post").orderBy("begeniSayisi", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception!=null){
                    Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }else{
                    if (snapshot!=null){
                        if (!snapshot.isEmpty){

                            val documents=snapshot.documents
                            postListesi.clear()
                            for (documents in documents){

                                val kullaniciAdi=documents.get("kullaniciAdi") as String
                                val talepDetayi=documents.get("talepdetayi") as String
                                val gorselUrl=documents.get("gorselurl") as String
                                val ilgiliKurum=documents.get("ilgilikurum") as String
                                val begeniSayisi=documents.get("begeniSayisi") as String

                                val indirilenPost= Post(kullaniciAdi,talepDetayi,ilgiliKurum,gorselUrl,begeniSayisi)
                                postListesi.add(indirilenPost)

                            }
                            recyclerViewAdapter.notifyDataSetChanged()

                        }
                    }
                    progressDialog.dismiss()
                }
            }
    }

    private val navigasZexly = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {

            R.id.paylasimYap -> {
                val intent=Intent(this, PaylasimYapActivity::class.java)
                startActivity(intent)

            }
            R.id.cikis -> {
                auth.signOut()
                val intent=Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.myprofil-> {
                val intent=Intent(this,MyProfilActivity::class.java)
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