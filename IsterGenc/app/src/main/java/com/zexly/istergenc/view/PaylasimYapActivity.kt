package com.zexly.istergenc.view

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.zexly.istergenc.R
import com.zexly.istergenc.model.Post
import kotlinx.android.synthetic.main.activity_paylasim_yap.*
import java.util.*

class PaylasimYapActivity : AppCompatActivity() {
    var secilenGorsel: Uri?=null
    var secilenBitmap: Bitmap?=null
    private  lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var category: Spinner
    val postHashMap= hashMapOf<String,Any>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paylasim_yap)
        cardViewID.visibility= View.GONE
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        shareButon.setOnClickListener { paylas() }
        menu()

        signIBId.setOnClickListener {

            gorselSec()

        }
        spinner()
    }


    fun paylas(){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Yükleniyor,lütfen bekleyiniz...")
        progressDialog.show()
        //Depo İşlemleri
        //UUID-> universal unique id
        val uuid= UUID.randomUUID()
        val gorselIsmi="${uuid}.jpg"

        val reference=storage.reference

        val gorselReference=reference.child("images").child(gorselIsmi)
        val guncelKullaniciEmaili=auth.currentUser!!.email.toString()


        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot->
                val yuklenenGorselRefrence= FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselRefrence.downloadUrl.addOnSuccessListener {

                    database.collection("Kullaniciler")
                        .addSnapshotListener { snapshot, exception ->
                            if(exception!=null){
                                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                            }else{
                                if (snapshot!=null){
                                    if (!snapshot.isEmpty){

                                        val documents=snapshot.documents
                                        for (documents in documents){
                                            if(guncelKullaniciEmaili == documents.get("kullaniciemail") as String){

                                               val  kullaniciAdi=documents.get("kullaniciAdi") as String

                                                val downloadUrl=it.toString()
                                                val talepDetayi=talepDetay.text.toString()
                                                val ilgiliKurum=kurumSpinner.selectedItem.toString()
                                                val begeni= (2000..5000).random().toString()

                                                //veri tabanı işlemleri

                                                postHashMap.put("gorselurl",downloadUrl)
                                                postHashMap.put("kullaniciAdi",kullaniciAdi)
                                                postHashMap.put("talepdetayi",talepDetayi)
                                                postHashMap.put("ilgilikurum",ilgiliKurum)
                                                postHashMap.put("begeniSayisi",begeni)

                                                database.collection("Post").add(postHashMap).addOnCompleteListener {
                                                    if(it.isSuccessful){
                                                        finish()
                                                        progressDialog.cancel()
                                                    }
                                                }.addOnFailureListener {
                                                    Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }


                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if(it.isSuccessful){
                            finish()
                            progressDialog.cancel()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

                }
            }.addOnFailureListener{
                Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }else{

            database.collection("Kullaniciler")
                .addSnapshotListener { snapshot, exception ->
                    if(exception!=null){
                        Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }else{
                        if (snapshot!=null){
                            if (!snapshot.isEmpty){
                                val documents=snapshot.documents
                                for (documents in documents){
                                    if(guncelKullaniciEmaili == documents.get("kullaniciemail") as String){

                                        val kullaniciAdi=documents.get("kullaniciAdi") as String
                                        val downloadUrl="null"
                                        val talepDetayi=talepDetay.text.toString()
                                        val ilgiliKurum=kurumSpinner.selectedItem.toString()
                                        val begeni= (2000..5000).random().toString()
                                        //veri tabanı işlemleri
                                        postHashMap.put("gorselurl",downloadUrl)
                                        postHashMap.put("kullaniciAdi",kullaniciAdi)
                                        postHashMap.put("talepdetayi",talepDetayi)
                                        postHashMap.put("ilgilikurum",ilgiliKurum)
                                        postHashMap.put("begeniSayisi",begeni)
                                        database.collection("Post").add(postHashMap).addOnCompleteListener {
                                            if(it.isSuccessful){
                                                finish()
                                                progressDialog.cancel()
                                            }
                                        }.addOnFailureListener {
                                            Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
        }
    }

    fun gorselSec(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //izin alınmalı
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val galeriIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)

        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.size>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar
                val galeriIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2&&resultCode== Activity.RESULT_OK&&data!=null){

            secilenGorsel= data.data

            if (secilenGorsel!=null){
                if (Build.VERSION.SDK_INT>=28){
                    cardViewID.visibility= View.VISIBLE
                    val source= ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap= ImageDecoder.decodeBitmap(source)
                    imageViewGorselSec.setImageBitmap(secilenBitmap)

                }else {
                    cardViewID.visibility= View.VISIBLE
                    secilenBitmap=
                        MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageViewGorselSec.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun spinner()
    {
        category = kurumSpinner!!.findViewById(R.id.kurumSpinner)

        val lessonsList: MutableList<String> = mutableListOf("AİLE VE SOSYAL HİZMETLER BAKANLIĞI","ÇALIŞMA VE SOSYAL GÜVENLİK BAKANLIĞI",
            "ÇEVRE, ŞEHİRCİLİK VE İKLİM DEĞİŞİKLİĞİ BAKANLIĞI",
            "DIŞİŞLERİ BAKANLIĞI","ENERJİ VE TABİİ KAYNAKLAR BAKANLIĞI",
            "GENÇLİK VE SPOR BAKANLIĞI","HAZİNE VE MALİYE BAKANLIĞI",
            "KÜLTÜR VE TURİZM BAKANLIĞI","MİLLİ EĞITIM BAKANLIĞI (MEB)","MİLLİ SAVUNMA BAKANLIĞI (MSB)",
            "SAĞLIK BAKANLIĞI","SANAYİ VE TEKNOLOJİ BAKANLIĞI","TARIM VE ORMAN BAKANLIĞI","ULAŞTIRMA VE ALTYAPI BAKANLIĞI")

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,lessonsList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        category.adapter= adapter

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
            R.id.home-> {
                val intent=Intent(this,AnaSayfaActivity::class.java)
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