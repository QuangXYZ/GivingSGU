package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgu.givingsgu.model.Project
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.network.response.LoginResponse
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.ProjectRepository
import com.sgu.givingsgu.utils.DataLocalManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val projectRepository = ProjectRepository()

    private val _project = MutableLiveData<List<ProjectResponse>>()
    val project: LiveData<List<ProjectResponse>> get() = _project

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchAllProject() {
        viewModelScope.launch {
            projectRepository.getAllProjects().enqueue(object :
                Callback<ResponseWrapper<List<ProjectResponse>>> {
                override fun onResponse(call: Call<ResponseWrapper<List<ProjectResponse>>>, response: Response<ResponseWrapper<List<ProjectResponse>>>) {
                    if (response.isSuccessful) {
                        _project.value = response.body()?.data!!
                    } else {
                        _error.value = response.message()
                    }
                }
                override fun onFailure(call: Call<ResponseWrapper<List<ProjectResponse>>>, t: Throwable) {
                    _error.value = t.message

                }
            })


        }
    }
}