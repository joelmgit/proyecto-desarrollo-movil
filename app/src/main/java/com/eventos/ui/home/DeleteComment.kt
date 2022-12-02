package com.eventos.ui.home

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
import com.eventos.databinding.FragmentAddCommentBinding
import com.eventos.databinding.FragmentDeleteCommentBinding
import com.eventos.viewmodel.CommentsViewModel
import com.eventos.viewmodel.HomeViewModel

class DeleteComment : Fragment() {

    private val args by navArgs<DeleteCommentArgs>()

    private var _binding: FragmentDeleteCommentBinding? = null

    private val binding get() = _binding!!

    private lateinit var commentsViewModel: CommentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commentsViewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        _binding = FragmentDeleteCommentBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_deleteComment_to_eventDetail)
        }

        binding.deleteCommentButton.setOnClickListener { deleteCommment() }

        return binding.root
    }

    private fun deleteCommment() {
        commentsViewModel.deleteEventComment(args.event, args.comment)
        Toast.makeText(requireContext(),getString(R.string.commentDeletedAlert), Toast.LENGTH_SHORT).show()
        val nav = DeleteCommentDirections.actionDeleteCommentToEventDetail(args.event)
        findNavController().navigate(nav)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}