package com.eventos.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eventos.MainActivity
import com.eventos.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener{signUp()}

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUp() {
        val email = binding.signUpEmailTextField.text.toString()
        val pass = binding.signUpPasswordTextField.text.toString()
        val fullName = binding.signUpfullNameTextField.text.toString()


        if(email.isNotEmpty() && pass.isNotEmpty() && fullName.isNotEmpty()){
            (activity as MainActivity).auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                if(it.isSuccessful){
                    val user = (activity as MainActivity).auth.currentUser

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user!!.updateProfile(profileUpdates).addOnCompleteListener {}.addOnFailureListener{
                        Toast.makeText((activity as MainActivity), "Error al registrarse.", Toast.LENGTH_LONG).show()
                    }
                    (activity as MainActivity).updateLoggedUser(user)
                }else{
                    Toast.makeText((activity as MainActivity), "Error al registrarse.", Toast.LENGTH_LONG).show()
                }
                }
        }else{
            Toast.makeText(super.requireContext(), "Por favor ingrese un nombre completo, correo electrónico y contraseña válido.", Toast.LENGTH_LONG).show()
        }

    }
}