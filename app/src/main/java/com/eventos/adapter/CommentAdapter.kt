package com.eventos.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eventos.R
import com.eventos.databinding.FragmentCommentItemBinding
import com.eventos.model.EventComment
import com.eventos.model.EventPost
import com.eventos.ui.home.EventDetailDirections
import com.eventos.ui.home.EventsFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class CommentAdapter constructor(context:Context, eventPost: EventPost) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var commentList = emptyList<EventComment>()
    private val authInstance: FirebaseAuth = FirebaseAuth.getInstance()
    private val deleteCommentContext = context
    private val currentEvent = eventPost

    inner class CommentViewHolder(private val itemBinding: FragmentCommentItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun render(comment: EventComment) {
            itemBinding.eventCommentAuthor.text = comment.authorName
            itemBinding.eventCommentBody.text = comment.body
            itemBinding.eventCommentDateTime.text = comment.commentDateTime
            itemBinding.eventCommentLocation.text = comment.latitude.toString() + ", " + comment.longitude.toString()
            if(authInstance.currentUser!!.uid == comment.authorId){
                itemBinding.deleteEventCommentButton.visibility = View.VISIBLE
                itemBinding.deleteEventCommentButton.setOnClickListener {
                    val nav = EventDetailDirections.actionEventDetailToDeleteComment(comment, currentEvent)
                    itemView.findNavController().navigate(nav)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemBinding = FragmentCommentItemBinding
            .inflate(
                LayoutInflater.from(parent.context)
                ,parent
                ,false)
        return CommentViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentEvent = commentList[position]
        holder.render(currentEvent)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun setComments(comments : List<EventComment>) {
        commentList = comments
        notifyDataSetChanged()  //Se notifica que el conjunto de datos cambio y se redibuja toda la lista
    }

}