package com.sgu.givingsgu.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sgu.givingsgu.model.User

object DataLocalManager {

    private const val PREFS_NAME = "MyAppPreferences"
    private const val KEY_USER = "user"
    private const val KEY_TOKEN = "token"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    private var sharedPreferences: SharedPreferences? = null
    private val gson = Gson()

    // Khởi tạo `SharedPreferences`
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    // Lưu đối tượng User
    fun saveUser(user: User) {
        val json = gson.toJson(user)
        sharedPreferences?.edit()?.putString(KEY_USER, json)?.apply()
    }

    // Lấy đối tượng User
    fun getUser(): User? {
        val json = sharedPreferences?.getString(KEY_USER, null)
        return gson.fromJson(json, User::class.java)
    }

    // Lưu token
    fun saveToken(token: String) {
        sharedPreferences?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    // Lấy token
    fun getToken(): String? {
        return sharedPreferences?.getString(KEY_TOKEN, null)
    }


    // Lưu trạng thái đăng nhập
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences?.edit()?.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)?.apply()
    }

    // Kiểm tra trạng thái đăng nhập
    fun isLoggedIn(): Boolean {
        return sharedPreferences?.getBoolean(KEY_IS_LOGGED_IN, false) ?: false
    }

    fun clearTokenAndUser() {
        sharedPreferences?.edit()?.remove(KEY_TOKEN)?.remove(KEY_USER)?.apply()
    }


    // Xóa toàn bộ dữ liệu
    fun clearData() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
