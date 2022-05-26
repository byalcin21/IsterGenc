package com.zexly.istergenc.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.zexly.istergenc.R
import kotlinx.android.synthetic.main.fragment_sign_up_part1.*


class SignUpPart1Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_part1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goto_part2_SignUpFragment.setOnClickListener {

            val tcKimlikNo=tcNoText.text.toString()
            val isim=isimText.text.toString()
            val soyisim=soyisimText.text.toString()
            val telNo=telNoText.text.toString()

            if (!tcKimlikNo.isEmpty() && !isim.isEmpty() && !soyisim.isEmpty() && !telNo.isEmpty()){
                if (tcKimlikNo.length==11){

                    val action=SignUpPart1FragmentDirections.actionSignUpPart1FragmentToSignUpPart2Fragment(tcKimlikNo,isim,soyisim,telNo)
                    Navigation.findNavController(it).navigate(action)
                }
                else
                {
                    Toast.makeText(requireContext(),"TC Kimlik Numarası 11 Hane Olmalıdır", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext(),"Alanların Hiç Biri Boş Geçilemez", Toast.LENGTH_SHORT).show()
            }

        }

        goto_login_fragment.setOnClickListener {
            val action=SignUpPart1FragmentDirections.actionSignUpPart1FragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }
}