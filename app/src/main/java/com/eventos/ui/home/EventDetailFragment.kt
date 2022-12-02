package com.eventos.ui.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventos.R
import com.eventos.adapter.CommentAdapter
import com.eventos.adapter.EventPostAdapter
import com.eventos.databinding.FragmentEventDetailBinding
import com.eventos.model.EventComment
import com.eventos.viewmodel.CommentsViewModel
import com.eventos.viewmodel.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import androidx.navigation.fragment.findNavController

class EventDetail : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private var mapReady = false

    private val args by navArgs<EventDetailArgs>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.eventLocationMap.onCreate(savedInstanceState)
        binding.eventLocationMap.onResume()
        binding.eventLocationMap.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
            mapReady = true
            updateMap()
        }
    }

    private fun updateMap(){
        if(args.event.longitude.isFinite() && args.event.latitude.isFinite()){
            val marker =
                com.google.android.gms.maps.model.LatLng(args.event.latitude, args.event.longitude)
            googleMap.addMarker(MarkerOptions().position(marker).title(args.event.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val commentsViwModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)

        binding.eventDetailNameTextView.text = args.event.name
        binding.eventDetailPhoneTextView.text = args.event.phoneNumber
        binding.eventDetailDescriptionTextView.text = args.event.description
        binding.eventDetailDateTextView.text = args.event.eventDate
        binding.eventDetailActivitiesTextView.text = args.event.activities

        val commentAdapter = CommentAdapter(this.requireContext(), args.event)
        val recycler = binding.eventCommentsRecyclerView
        recycler.adapter = commentAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val comments : LiveData<List<EventComment>> = commentsViwModel.getEventComments(args.event)

        comments.observe(viewLifecycleOwner){
                eachComment -> commentAdapter.setComments(eachComment)
        }

        binding.commentEventButton.setOnClickListener {
            val nav = EventDetailDirections.actionEventDetailToAddComment(args.event)
            findNavController().navigate(nav)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}