package com.sgu.givingsgu.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.activity.DonationHistoryActivity
import com.sgu.givingsgu.model.Page
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.network.response.TransactionResponse
import com.sgu.givingsgu.repository.DonationRepository
import com.sgu.givingsgu.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNameMap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class DonationHistoryViewModel : ViewModel() {
    private val repository = TransactionRepository()

    private val _transaction = MutableLiveData<List<TransactionResponse>>()
    val transaction: LiveData<List<TransactionResponse>> get() = _transaction

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val totalPages = MutableLiveData<Int>()
    val currentPage = MutableLiveData<Int>()


    fun fetchTransaction(
        projectId: Long,
        page: Int,
    ) {
        viewModelScope.launch {
            repository.getTransactionByProjectId(projectId,page,10)
                .enqueue(object : Callback<ResponseWrapper<Page<TransactionResponse>>> {
                    override fun onResponse(
                        call: Call<ResponseWrapper<Page<TransactionResponse>>>,
                        response: Response<ResponseWrapper<Page<TransactionResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()?.data
                            _transaction.postValue(data?.content ?: emptyList())
                            currentPage.postValue(data?.number ?: 0)
                            totalPages.postValue(data?.totalPages ?: 0)

                        } else {
                             _error.value = response.message()
                        }
                    }
                    override fun onFailure(call: Call<ResponseWrapper<Page<TransactionResponse>>>, t: Throwable) {
                         _error.value = t.message
                    }
                })
        }



    }


    fun exportTransactionsToExcel(projectId: Long, callback: OnExportExcelListener) {
        viewModelScope.launch {
            repository.exportTransactionsToExcel(projectId)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null) {
                                callback.onExportExcelSuccess(body)
                            }
                        } else {
                            _error.value = response.message()
                            callback.onExportExcelFailed(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        _error.value = t.message
                    }
                })
        }
    }


    interface OnExportExcelListener {
        fun onExportExcelSuccess(response: ResponseBody)
        fun onExportExcelFailed(message: String)
    }



}