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

class UpdateProjectViewModel : ViewModel()  {
    private val _updateResult = MutableLiveData<Result<ProjectDTO>>()
    val updateResult: LiveData<Result<ProjectDTO>> get() = _updateResult

    private val projectRepository = ProjectRepository()

    // Hàm upload ảnh lên Firebase và cập nhật dự án
    fun uploadImagesAndUpdateProject(id: Long, imageUris: List<Uri>, project: ProjectDTO) {
        if (imageUris.isEmpty()) {
            // Nếu không có ảnh, chỉ cập nhật dự án mà không có URL ảnh
            updateProject(id, project)
            return
        }

        uploadImagesToFirebase(imageUris) { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                val projectWithImages = project.copy(imageUrls = imageUrls.joinToString(", "))
                updateProject(id, projectWithImages)
            } else {
                // Nếu không có ảnh nào được upload thành công, tạo project không có URL ảnh
                updateProject(id, project)
            }
        }
    }

    private fun updateProject(id: Long, project: ProjectDTO) {
        viewModelScope.launch {
            try {
                val response = projectRepository.updateProject(id, project)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _updateResult.postValue(Result.success(body))
                    } else {
                        _updateResult.postValue(Result.failure(Exception("Phản hồi từ server không có dữ liệu")))
                    }
//                    response.body()?.let {
//                        _updateResult.postValue(Result.success(it))
//                    } ?: _updateResult.postValue(Result.failure(Exception("Phản hồi từ server không có dữ liệu")))
                } else {
                    _updateResult.postValue(Result.failure(Exception("Cập nhật dự án thất bại: ${response.message()}")))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Result.failure(e))
            }
        }
    }

    // Hàm upload ảnh lên Firebase như đã mô tả trong mẫu
    private fun uploadImagesToFirebase(imageUris: List<Uri>, onComplete: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>()
        val storageRef = FirebaseStorage.getInstance().reference.child("project_images")
        val tasks = mutableListOf<Task<Uri>>()

        imageUris.forEach { uri ->
            val fileRef = storageRef.child(UUID.randomUUID().toString())
            val uploadTask = fileRef.putFile(uri)
            tasks.add(uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                fileRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                imageUrls.add(downloadUri.toString())
            })
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                onComplete(if (it.isSuccessful) imageUrls else emptyList())
            }
    }
}