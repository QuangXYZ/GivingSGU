package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgu.givingsgu.model.Project
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.repository.ProjectRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val projectRepository = ProjectRepository()

    private val _project = MutableLiveData<List<Project>>()
    val project: LiveData<List<Project>> get() = _project

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchAllProject() {
        viewModelScope.launch {
            try {
                val result = projectRepository.getAllProjects()
                _project.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}