package com.eventos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eventos.databinding.FragmentEventItemBinding
import com.eventos.model.EventPost
import com.eventos.ui.home.EventsFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class EventPostAdapter : RecyclerView.Adapter<EventPostAdapter.EventPostViewHolder>() {

    private var eventsList = emptyList<EventPost>()
    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()

    inner class EventPostViewHolder(private val itemBinding: FragmentEventItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun render(eventPost: EventPost) {
            itemBinding.eventNameLabel.text = eventPost.name
            itemBinding.eventOrganizerNameLabel.text = eventPost.organizerName
            itemBinding.eventDateLabel.text = eventPost.eventDate
            if(authInstance.currentUser!!.uid == eventPost.organizerId){
                itemBinding.eventEditButton.visibility = View.VISIBLE
                itemBinding.eventEditButton.setOnClickListener {
                    val nav = EventsFragmentDirections.actionNavHomeToUpdateEvent(eventPost)
                    itemView.findNavController().navigate(nav)
                }
            }
            itemBinding.eventCardView.setOnClickListener {
                val nav = EventsFragmentDirections.actionNavHomeToEventDetail(eventPost)
                itemView.findNavController().navigate(nav)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPostViewHolder {
        val itemBinding = FragmentEventItemBinding
            .inflate(
                LayoutInflater.from(parent.context)
                ,parent
                ,false)
        return EventPostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: EventPostViewHolder, position: Int) {
        val currentEvent = eventsList[position]
        holder.render(currentEvent)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    fun setEvents(events : List<EventPost>) {
        eventsList = events
        notifyDataSetChanged()  //Se notifica que el conjunto de datos cambio y se redibuja toda la lista
    }

    fun filterEvents(events : List<EventPost>, filterParameter:String) {
        eventsList = events.filter { it.name == filterParameter}
        notifyDataSetChanged()  //Se notifica que el conjunto de datos cambio y se redibuja toda la lista
    }

}