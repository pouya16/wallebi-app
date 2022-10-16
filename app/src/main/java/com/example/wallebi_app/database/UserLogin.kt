package com.example.wallebi_app.database

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.wallebi_app.models.UserModel

class UserLogin(context:Context) {

    var sharedPreferences: SharedPreferences

    init {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            "shared_login",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isUser(): Boolean{

    }

    fun saveUser(userModel: UserModel): Boolean{
        try {
            sharedPreferences.edit()
                .putString("user", userModel.userName)
                .putString("password", userModel.password)
                .putString("email", userModel.email)
                .putString("mobile", userModel.mobile)
                .apply()
        }catch (e: Exception){return false}
        return true;
    }

    fun getUser(): UserModel{
        return UserModel(
            sharedPreferences.getString("user","")!!,
            sharedPreferences.getString("password","")!!,
            sharedPreferences.getString("email","")!!,
            sharedPreferences.getString("mobile","")!!
        )
    }


}



