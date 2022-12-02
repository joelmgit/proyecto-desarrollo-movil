package com.eventos.repository

import androidx.lifecycle.LiveData
import com.eventos.data.EventPostDAO
import com.eventos.model.EventComment
import com.eventos.model.EventPost

class EventPostRepository (private val eventPostDAO: EventPostDAO) {
    fun saveEvent(event: EventPost) {eventPostDAO.saveEvent(event)}
    fun deleteEvent(event: EventPost) {eventPostDAO.deleteEvent(event)}
    val getEvents : LiveData<List<EventPost>> = eventPostDAO.getEvents()
    fun getEventComments(event: EventPost) : LiveData<List<EventComment>> {return eventPostDAO.getEventComments(event)}
    fun saveEventComment(event: EventPost, comment: EventComment) {eventPostDAO.saveEventComment(event, comment)}
    fun deleteEventComment(event: EventPost, comment: EventComment) {eventPostDAO.deleteEventComment(event, comment)}
}