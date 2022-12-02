package com.eventos.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eventos.R
import com.eventos.adapter.EventPostAdapter
import com.eventos.databinding.FragmentEventsBinding
import com.eventos.viewmodel.HomeViewModel

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)

        val eventPostAdapter = EventPostAdapter()
        val recycler = binding.eventsRecyclerView
        recycler.adapter = eventPostAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.getEvents.observe(viewLifecycleOwner){
                events -> eventPostAdapter.setEvents(events)
        }

        binding.addEventNavigationButton.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))

        binding.addEventNavigationButton.setOnClickListener{
            findNavController().navigate(R.id.action_nav_events_to_addEvent)
        }

        binding.filterEventsButton.setOnClickListener {
            if(binding.filterEventsTextField.text.isNotEmpty()){
                eventPostAdapter.filterEvents(homeViewModel.getEvents.value!!, binding.filterEventsTextField.text.toString())
            }else{
                Toast.makeText(requireContext(),getString(R.string.filterErrorAlert), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}