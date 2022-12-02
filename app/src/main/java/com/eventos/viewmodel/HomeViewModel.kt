package com.eventos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventos.data.EventPostDAO
import com.eventos.model.EventPost
import com.eventos.repository.EventPostRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository: EventPostRepository = EventPostRepository(EventPostDAO())

    val getEvents = repository.getEvents
}