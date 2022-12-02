package com.eventos.ui.home

import android.Manifest
import android.app.AlertDialog
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
import com.eventos.databinding.FragmentUpdateEventBinding
import com.eventos.model.EventPost
import com.eventos.viewmodel.AddEventViewModel
import com.eventos.viewmodel.UpdateEventViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class UpdateEvent : Fragment() {

    private var _binding: FragmentUpdateEventBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<UpdateEventArgs>()

    private var eventLongitude : Double = 0.0
    private var eventLatitude : Double = 0.0
    private var eventAltitude : Double = 0.0

    private lateinit var updateEventViewModel: UpdateEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        updateEventViewModel = ViewModelProvider(this).get(UpdateEventViewModel::class.java)
        _binding = FragmentUpdateEventBinding.inflate(inflater, container, false)


        binding.updateEventNameTextField.setText(args.event.name)
        binding.updateEventPhoneTextField.setText(args.event.phoneNumber)
        binding.updateEventDescriptionTextField.setText(args.event.description)
        binding.updateEventDateTextField.setText(args.event.eventDate)
        binding.updateEventActivitiesTextField.setText(args.event.activities)

        binding.updateEventButton.setOnClickListener { updateEvent() }
        binding.deleteEventButton.setOnClickListener { deleteEvent() }

        activateGPS()

        return binding.root
    }


    private fun updateEvent() {
        val eventName = binding.updateEventNameTextField.text.toString()
        val eventPhone = binding.updateEventPhoneTextField.text.toString()
        val eventDescription = binding.updateEventDescriptionTextField.text.toString()
        val eventDate = binding.updateEventDateTextField.text.toString()
        val eventActivities = binding.updateEventActivitiesTextField.text.toString()

        requestLocation()

        if(eventName.isNotEmpty() && eventPhone.isNotEmpty() && eventDescription.isNotEmpty() && eventDate.isNotEmpty() && eventActivities.isNotEmpty()){
            val event = EventPost(
                args.event.id,
                eventName,
                args.event.organizerId,
                args.event.organizerName,
                eventPhone,
                eventDescription,
                eventLatitude,
                eventLongitude,
                eventAltitude,
                eventDate,
                eventActivities
            )
            updateEventViewModel.saveEvent(event)
            Toast.makeText(requireContext(),getString(R.string.updateEventAlert), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateEvent_to_nav_home)
        }else{
            Toast.makeText(requireContext(),getString(R.string.updateEventErrorAlert), Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteEvent() {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle(R.string.deleteEventButton)
        alert.setMessage(getString(R.string.confirmDelete) + "${args.event.name}?")
        alert.setPositiveButton(getString(R.string.yesConfirmation)){_,_ ->
            updateEventViewModel.deleteEvent(args.event)
            Toast.makeText(requireContext(),getString(R.string.deleteEventAlert), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateEvent_to_nav_home)
        }
        alert.setNegativeButton(getString(R.string.noConfirmation)){_,_ ->}
        alert.create().show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}