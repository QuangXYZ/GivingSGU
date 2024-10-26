package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.network.request.TransactionRequest
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


class DonationViewModel : ViewModel() {
    private val repository = TransactionRepository()



    fun saveTransaction(userId: Long, projectId: Long, amount: Double, paymentMethod: String) {
        val transactionRequest = TransactionRequest(userId, projectId, amount, paymentMethod)

        viewModelScope.launch {
            repository.submitTransaction(transactionRequest).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Xử lý thành công
                    } else {
                        // Xử lý thất bại
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi
                }
            })

        }



        }
}