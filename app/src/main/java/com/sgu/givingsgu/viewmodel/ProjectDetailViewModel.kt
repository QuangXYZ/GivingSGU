package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.repository.CommentRepository
import com.sgu.givingsgu.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectDetailViewModel : ViewModel() {
    private val commentRepository = CommentRepository()

    private val _comment = MutableLiveData<List<CommentResponse>>()
    val comment: LiveData<List<CommentResponse>> get() = _comment

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchAllComment(projectId:Long) {
        viewModelScope.launch {
            try {
                val result = commentRepository.getCommentsByProjectId(projectId)
                _comment.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}