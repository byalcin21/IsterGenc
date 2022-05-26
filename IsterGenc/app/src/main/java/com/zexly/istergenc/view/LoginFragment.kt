package com.zexly.istergenc.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.zexly.istergenc.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goto_sign_up_fragment.setOnClickListener {
            val action=LoginFragmentDirections.actionLoginFragmentToSignUpPart1Fragment()
            Navigation.findNavController(it).navigate(action)
        }

        girisYapButon.setOnClickListener {
            girisYap()
        }

    }

    fun girisYap(){

        val email=emailText.text.toString()
        val sifre=PasswordText.text.toString()

        if (emailText.text!= null && !emailText.text.trim().isEmpty()&&PasswordText.text!= null && !PasswordText.text.trim().isEmpty()) {

            auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //diğer aktiviteye git
                    val guncelKullanici = auth.currentUser?.email.toString()
                    Toast.makeText(requireContext(), "Hoşgeldiniz by ${guncelKullanici}", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(requireContext(), AnaSayfaActivity::class.java)
                    startActivity(intent)
                    activity?.onBackPressed();
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Kullanıcı email veya şifre hatalı!", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Email ve Şifre Alanları Boş Geçilemez", Toast.LENGTH_LONG).show()

        }
    }
}