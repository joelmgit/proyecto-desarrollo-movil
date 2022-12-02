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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eventos.R
import com.eventos.databinding.FragmentAddEventBinding
import com.eventos.model.EventPost
import com.eventos.viewmodel.AddEventViewModel
import com.eventos.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class AddEvent : Fragment() {
    private var _binding: FragmentAddEventBinding? = null

    private val binding get() = _binding!!

    private var eventLongitude : Double = 0.0
    private var eventLatitude : Double = 0.0
    private var eventAltitude : Double = 0.0

    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var addEventViewModel: AddEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addEventViewModel = ViewModelProvider(this).get(AddEventViewModel::class.java)

        _binding = FragmentAddEventBinding.inflate(inflater, container, false)

        binding.addEventButton.setOnClickListener { addEvent() }

        activateGPS()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                eventLatitude = location.latitude
                eventLongitude = location.longitude
                eventAltitude = location.altitude
            }
        }
    }

    private fun addEvent() {
        val eventName = binding.eventNameTextField.text.toString()
        val eventPhone = binding.eventPhoneTextField.text.toString()
        val eventDescription = binding.eventDescriptionTextField.text.toString()
        val eventDate = binding.eventDateTextField.text.toString()
        val eventActivities = binding.eventActivitiesTextField.text.toString()

        requestLocation()

        if(eventName.isNotEmpty() && eventPhone.isNotEmpty() && eventDescription.isNotEmpty() && eventDate.isNotEmpty() && eventActivities.isNotEmpty()){
            val event = EventPost(
                "",
                eventName,
                authInstance.currentUser?.uid!!,
                authInstance.currentUser?.displayName!!,
                eventPhone,
                eventDescription,
                eventLatitude,
                eventLongitude,
                eventAltitude,
                eventDate,
                eventActivities
            )
            addEventViewModel.saveEvent(event)
            Toast.makeText(requireContext(),getString(R.string.eventAddedAlert), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addEvent_to_nav_home)
        }else{
            Toast.makeText(requireContext(),getString(R.string.addEventErrorAlert), Toast.LENGTH_SHORT).show()
        }
    }
}