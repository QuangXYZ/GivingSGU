package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ResponseWrapper
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


    fun saveTransaction(
        transactionId: String,
        userId: Long,
        projectId: Long,
        amount: Double,
        paymentMethod: String,
        token: String, transactionCallback: TransactionCallback
    ) {
        val transactionRequest =
            TransactionRequest(transactionId, projectId, userId, amount, paymentMethod, token)

        viewModelScope.launch {
            repository.submitTransaction(transactionRequest)
                .enqueue(object : Callback<ResponseWrapper<Transaction>> {
                    override fun onResponse(
                        call: Call<ResponseWrapper<Transaction>>,
                        response: Response<ResponseWrapper<Transaction>>
                    ) {
                        if (response.isSuccessful) {
                            // Xử lý thành công
                            transactionCallback.onSuccess(response.body()?.data)


                        } else {
                            transactionCallback.onFail(response.message())
                            // Xử lý thất bại
                        }
                    }

                    override fun onFailure(call: Call<ResponseWrapper<Transaction>>, t: Throwable) {
                        transactionCallback.onFail(t.message.toString()) }
                })

        }


    }

    interface TransactionCallback {
        fun onSuccess(transaction: Transaction?)
        fun onFail(error : String)
    }
}