package com.sgu.givingsgu.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.repository.ProjectRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AdminHomeViewModel : ViewModel() {

    private val _creationResult = MutableLiveData<Result<ProjectDTO>>()
    val creationResult: LiveData<Result<ProjectDTO>> get() = _creationResult

    private val projectRepository = ProjectRepository()


    // Hàm upload ảnh lên Firebase và tạo dự án
    fun uploadImagesAndCreateProject(imageUris: List<Uri>, project: ProjectDTO) {
        if (imageUris.isEmpty()) {
            // Nếu không có ảnh, vẫn tạo dự án nhưng không có URL ảnh
            createProject(project)
            return
        }

        uploadImagesToFirebase(imageUris) { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                val projectWithImages = project.copy(imageUrls = imageUrls.joinToString(", "))
                createProject(projectWithImages)
            } else {
                // Nếu không có ảnh nào được upload thành công, tạo project không có URL ảnh
                createProject(project)
            }
        }
    }

    // Hàm upload ảnh lên Firebase
    private fun uploadImagesToFirebase(imageUris: List<Uri>, onComplete: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>()
        val storageRef = FirebaseStorage.getInstance().reference.child("project_images")
        val tasks = mutableListOf<Task<Uri>>()

        imageUris.forEach { uri ->
            val fileRef = storageRef.child(UUID.randomUUID().toString())
            val uploadTask = fileRef.putFile(uri)
            tasks.add(uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                imageUrls.add(downloadUri.toString())
            })
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onComplete(imageUrls)
                } else {
                    // Xử lý lỗi
                }
            }
    }

    // Gọi API tạo dự án
    private fun createProject(project: ProjectDTO) {

        viewModelScope.launch {
            try {

                val response = projectRepository.createProject(project)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _creationResult.postValue(Result.success(body))
                    } else {
                        _creationResult.postValue(Result.failure(Exception("Phản hồi từ server không có dữ liệu")))
                    }
                } else {
                    _creationResult.postValue(Result.failure(Exception("Tạo dự án thất bại: ${response.message()}")))
                }
            } catch (e: Exception) {
                _creationResult.postValue(Result.failure(e))
            }
        }
    }

}

