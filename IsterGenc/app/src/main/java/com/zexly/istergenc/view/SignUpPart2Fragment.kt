package com.zexly.istergenc.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.zexly.istergenc.R
import kotlinx.android.synthetic.main.fragment_sign_up_part2.*

class SignUpPart2Fragment : Fragment() {

    private  lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    lateinit var tcKimlikNo:String
    lateinit var isim:String
    lateinit var soyisim:String
    lateinit var telNo:String
    lateinit var email:String
    lateinit var password:String
    lateinit var kullaniciAdi:String
    lateinit var tarih: Timestamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_part2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goto_login_activity2.setOnClickListener {
            val action=SignUpPart2FragmentDirections.actionSignUpPart2FragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
        arguments?.let {
            //Argüman varsa kullan
            tcKimlikNo= SignUpPart2FragmentArgs.fromBundle(it).tcKimlikNo
            isim = SignUpPart2FragmentArgs.fromBundle(it).isim
            soyisim= SignUpPart2FragmentArgs.fromBundle(it).soyisim
            telNo= SignUpPart2FragmentArgs.fromBundle(it).telNo

        }

        signUpButton.setOnClickListener {
            kayitOl()
        }

    }

    fun kayitOl(){

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Yükleniyor,lütfen bekleyiniz...")
        progressDialog.show()

        email=emailTextTV.text.toString()
        password=passwordTextTV.text.toString()
        kullaniciAdi=kullaniciAdiTextTV.text.toString()
        tarih= Timestamp.now()

        //bilgileri firestore'a kaydetme

        //veri tabanı işlemleri



        if (emailTextTV.text!= null && !emailTextTV.text.trim().isEmpty()&&passwordTextTV.text!= null && !passwordTextTV.text.trim().isEmpty()) {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                //asenkron çalışır
                if(task.isSuccessful){
                    verikaydet()
                    Toast.makeText(requireContext(),"Kayıt başarılı", Toast.LENGTH_LONG).show()
                    progressDialog.cancel()
                    activity?.onBackPressed();
                    val intent= Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener { exception->
                progressDialog.cancel()
                Toast.makeText(requireContext(),exception.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
        else
        {
            Toast.makeText(requireContext(), "Email ve Şifre Alanları Boş Geçilemez", Toast.LENGTH_LONG).show()
            progressDialog.cancel()
        }

    }

    fun verikaydet(){
        val postHashMap= hashMapOf<String,Any>()
        postHashMap.put("kullaniciAdi",kullaniciAdi)
        postHashMap.put("kullaniciemail",email)
        postHashMap.put("tckimlikno",tcKimlikNo)
        postHashMap.put("telno",telNo)
        postHashMap.put("isim",isim)
        postHashMap.put("soyisim",soyisim)
        postHashMap.put("tarih",tarih)

        database.collection("Kullaniciler").add(postHashMap).addOnCompleteListener {
            if(it.isSuccessful){
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
        }


    }

}