package com.sgu.givingsgu.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.DonationResponse
import com.sgu.givingsgu.repository.CommentRepository
import com.sgu.givingsgu.repository.DonationRepository
import kotlinx.coroutines.launch


class ProjectDetailViewModel : ViewModel() {
    private val commentRepository = CommentRepository()
    private val donationRepository = DonationRepository()


    private val _comment = MutableLiveData<List<CommentResponse>>()
    val comment: LiveData<List<CommentResponse>> get() = _comment

    private val _donation = MutableLiveData<List<DonationResponse>>()
    val donation: LiveData<List<DonationResponse>> get() = _donation

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

    fun fetchTopDonors(projectId: Long) {
        viewModelScope.launch {
            try {
                val result = donationRepository.getTop10DonationsByProjectId(projectId)
                _donation.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}