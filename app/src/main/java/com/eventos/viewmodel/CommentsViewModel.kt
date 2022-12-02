package com.eventos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventos.data.EventPostDAO
import com.eventos.model.EventComment
import com.eventos.model.EventPost
import com.eventos.repository.EventPostRepository
import kotlinx.coroutines.launch

class CommentsViewModel : ViewModel() {
    private val repository: EventPostRepository = EventPostRepository(EventPostDAO())

    fun getEventComments(eventPost: EventPost) : LiveData<List<EventComment>> {
          return repository.getEventComments(eventPost)
    }
    fun deleteEventComment(eventPost: EventPost, comment: EventComment) {
        viewModelScope.launch { repository.deleteEventComment(eventPost, comment) }
    }
    fun saveEventComment(eventPost: EventPost, comment: EventComment) {
        viewModelScope.launch { repository.saveEventComment(eventPost, comment) }
    }
}