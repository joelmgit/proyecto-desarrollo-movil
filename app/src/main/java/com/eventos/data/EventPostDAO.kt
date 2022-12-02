package com.eventos.data

import android.app.AlertDialog
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.eventos.model.EventComment
import com.eventos.model.EventPost
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class EventPostDAO {
    private val eventsCollection = "events"
    private val commentsCollection = "comments"

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun saveEvent(eventPost: EventPost){
        val document: DocumentReference
        if(eventPost.id.isEmpty()){
            document = firestore.collection(eventsCollection).document()
            eventPost.id = document.id
        }else{
            document = firestore.collection(eventsCollection).document(eventPost.id)
        }
        document.set(eventPost).addOnSuccessListener {
            Log.d("saveEvent", "event saved/updated")
        }.addOnCanceledListener {
            Log.e("saveEvent", "error saving/updating event")
        }
    }

    fun deleteEvent(eventPost: EventPost){
        if(eventPost.id.isNotEmpty()){
            firestore.collection(eventsCollection).document(eventPost.id).delete().addOnSuccessListener {
                Log.d("deleteEvent", "event deleted")
            }.addOnCanceledListener {
                Log.e("deleteEvent", "error deleting event")
            }
        }
    }

    fun getEvents() : MutableLiveData<List<EventPost>> {
        val eventsList = MutableLiveData<List<EventPost>>()

        firestore.collection(eventsCollection).addSnapshotListener{
                snapshot, error ->
                if(error != null){
                return@addSnapshotListener
            }
            if(snapshot != null){
                val tmpList  = ArrayList<EventPost>()
                snapshot.documents.forEach{
                    val event = it.toObject(EventPost::class.java)
                    if(event != null){
                        tmpList.add(event)
                    }
                }
                eventsList.value = tmpList
            }
        }
        return eventsList
    }

    fun getEventComments(event: EventPost) : MutableLiveData<List<EventComment>> {
        val commentsList = MutableLiveData<List<EventComment>>()

        firestore.collection(eventsCollection).document(event.id).collection(commentsCollection).addSnapshotListener{
                snapshot, error ->
            if(error != null){
                return@addSnapshotListener
            }
            if(snapshot != null){
                val tmpList  = ArrayList<EventComment>()
                snapshot.documents.forEach{
                    val comment = it.toObject(EventComment::class.java)
                    if(comment != null){
                        tmpList.add(comment)
                    }
                }
                commentsList.value = tmpList
            }
        }
        return commentsList
    }


    fun deleteEventComment(eventPost: EventPost, comment: EventComment){
        if(eventPost.id.isNotEmpty()){
            firestore.collection(eventsCollection).document(eventPost.id).collection(commentsCollection).document(comment.id).delete().addOnSuccessListener {
                Log.d("deleteComment", "comment deleted")
            }.addOnCanceledListener {
                Log.e("deleteComment", "error deleting comment")
            }
        }
    }

    fun saveEventComment(eventPost: EventPost, comment: EventComment){
        val document: DocumentReference =
            firestore.collection(eventsCollection).document(eventPost.id).collection(commentsCollection).document()
        comment.id = document.id
        document.set(comment).addOnSuccessListener {
            Log.d("saveComment", "comment saved/updated")
        }.addOnCanceledListener {
            Log.e("saveComment", "error saving/updating comment")
        }
    }

}