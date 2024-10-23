package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNameMap


class DonationViewModel : ViewModel() {
    private var amount = "10000"
    private val fee = "0"
    var environment: Int = 0 //developer default
    private val merchantName = "Demo SDK"
    private val merchantCode = "SCB01"
    private val merchantNameLabel = "Nhà cung cấp"
    private val description = "Thanh toán dịch vụ ABC"

    init {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)
    }

//    private fun requestPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim()
//                .length() !== 0
//        ) amount = edAmount.getText().toString().trim()
//
//        val eventValue: MutableMap<String, Any> = HashMap()
//        //client Required
//        eventValue["merchantname"] =
//            merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue["merchantcode"] =
//            merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue["amount"] = total_amount //Kiểu integer
//        eventValue["orderId"] =
//            "orderId123456789" //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue["orderLabel"] = "Mã đơn hàng" //gán nhãn
//
//
//        //client Optional - bill info
//        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
//        eventValue["fee"] = total_fee //Kiểu integer
//        eventValue["description"] = description //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
//        eventValue["partnerCode"] = merchantCode
//        //Example extra data
//        val objExtraData = JSONObject()
//        try {
//            objExtraData.put("site_code", "008")
//            objExtraData.put("site_name", "CGV Cresent Mall")
//            objExtraData.put("screen_code", 0)
//            objExtraData.put("screen_name", "Special")
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
//            objExtraData.put("movie_format", "2D")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue["extraData"] = objExtraData.toString()
//
//        eventValue["extra"] = ""
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
//    }
//
//
//    private fun requestMapping() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.MAP)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.LINK)
//        val eventValue: MutableMap<String, Any> = HashMap()
//        //client Required
//        eventValue[MoMoParameterNameMap.CLIENT_ID] = clientId
//        eventValue[MoMoParameterNameMap.USER_NAME] = userName
//        eventValue[MoMoParameterNameMap.PARTNER_CODE] = partnerCode
//        //client info custom parameter
//        val objExtra = JSONObject()
//        try {
//            objExtra.put("key", "value")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue[MoMoParameterNameMap.EXTRA] = objExtra
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
//    }


}