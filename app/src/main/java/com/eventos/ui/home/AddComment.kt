package com.eventos.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eventos.R
import com.eventos.databinding.FragmentAddCommentBinding
import com.eventos.model.EventComment
import com.eventos.viewmodel.CommentsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddComment : Fragment() {

    private var _binding: FragmentAddCommentBinding? = null

    private val binding get() = _binding!!

    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()

    private val args by navArgs<AddCommentArgs>()

    private lateinit var commentsViewModel: CommentsViewModel

    private var commentLongitude : Double = 0.0
    private var commentLatitude : Double = 0.0
    private var commentAltitude : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commentsViewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        _binding = FragmentAddCommentBinding.inflate(inflater, container, false)

        binding.addCommentButton.setOnClickListener{ addComment() }

        activateGPS()

        return binding.root
    }

    private fun activateGPS() {
        if (
            requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                105
            )
        }
    }

    private fun requestLocation() {
        val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                    location : Location ->
                commentLatitude = location.latitude
                commentLongitude = location.longitude
                commentAltitude = location.altitude
            }
        }
    }

    private fun addComment() {
        val commentBody = binding.newCommentTextField.text.toString()
        val authorName = authInstance.currentUser?.displayName
        val authorId = authInstance.currentUser?.uid
        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm")
        val commentDate = formatter.format(localDateTime)

        requestLocation()

        if(commentBody.isNotEmpty()){
            val comment = EventComment(
                "",
                authorId!!,
                authorName!!,
                commentBody,
                commentLatitude,
                commentLongitude,
                commentAltitude,
                commentDate
            )
            commentsViewModel.saveEventComment(args.event, comment)
            Toast.makeText(requireContext(),getString(R.string.commentAddedAlert), Toast.LENGTH_SHORT).show()
            val nav = AddCommentDirections.actionAddCommentToEventDetail(args.event)
            findNavController().navigate(nav)
        }else{
            Toast.makeText(requireContext(),getString(R.string.addCommentErrorAlert), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}