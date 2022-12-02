package com.eventos.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eventos.MainActivity
import com.eventos.databinding.FragmentProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentEmail: String? = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.updateEmailTextField.setText(authInstance.currentUser?.email ?: "Error al recuperar correo electrónico")
        binding.updateFullNameTextFiled.setText(authInstance.currentUser?.displayName ?: "Error al recuperar nombre completo")

        binding.updateProfileButton.setOnClickListener { update() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun update() {
        val email = binding.updateEmailTextField.text.toString()
        val currentPass = binding.updateCurrentPasswordTextFiled.text.toString()
        val pass = binding.updatePasswordTextField.text.toString()
        val fullName = binding.updateFullNameTextFiled.text.toString()


        if(email.isNotEmpty() && pass.isNotEmpty() && fullName.isNotEmpty() && currentEmail!!.isNotEmpty()){

            Toast.makeText(context, email, Toast.LENGTH_LONG).show()

            val user = authInstance.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()

            val credential = EmailAuthProvider.getCredential(currentEmail, currentPass)

            user?.reauthenticate(credential)?.addOnCompleteListener {reAuthStatus ->
                if(reAuthStatus.isSuccessful){
                    val reAuthUser = FirebaseAuth.getInstance().currentUser
                    reAuthUser?.updateEmail(email)?.addOnCompleteListener { updateEmailStatus ->
                        if(updateEmailStatus.isSuccessful){
                            reAuthUser.updatePassword(pass).addOnCompleteListener { updatePasswordStatus ->
                                if(updatePasswordStatus.isSuccessful){
                                    user.updateProfile(profileUpdates).addOnCompleteListener {updateDisplayName ->
                                        if(updateDisplayName.isSuccessful){
                                            Toast.makeText(context, "Usuario actualizado correctamente.", Toast.LENGTH_LONG).show()
                                        }else{
                                            Toast.makeText(context, "Error al actualizar nombre completo.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, "Error al actualizar contraseña.", Toast.LENGTH_LONG).show()
                                }
                            }
                        }else{
                            Toast.makeText(context, updateEmailStatus.result.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(context, "Error al autentificar el usuario.", Toast.LENGTH_LONG).show()
                }

            }

        }else{
            Toast.makeText(context, "Por favor ingrese un nombre completo, correo electrónico y contraseña válido.", Toast.LENGTH_LONG).show()
        }
    }

}