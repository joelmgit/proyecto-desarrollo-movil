package com.eventos.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eventos.InnerPlatformActivity
import com.eventos.MainActivity
import com.eventos.R
import com.eventos.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpNavigationButton.setOnClickListener {
            findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
        }

        binding.signInbutton.setOnClickListener{signIn()}

    }

    private fun signIn() {
        val email = binding.signInEmailTextField.text.toString()
        val pass = binding.signInPasswordTextField.text.toString()

        if(email.isNotEmpty() && pass.isNotEmpty()){
            (activity as MainActivity).auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){
                if(it.isSuccessful){
                    val user = (activity as MainActivity).auth.currentUser
                    (activity as MainActivity).updateLoggedUser(user)
                }else{
                    Toast.makeText(super.requireContext(), "Error al iniciar sesión.", Toast.LENGTH_LONG).show()
                    (activity as MainActivity).updateLoggedUser(null)
                }
            }
        }else{
            Toast.makeText(super.requireContext(), "Por favor ingrese un correo electrónico y contraseña válidos.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).updateLoggedUser((activity as MainActivity).auth.currentUser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}