package com.example.wallebi_app.api.reg.model
import android.util.Log
import com.example.wallebi_app.database.LoginData

class PermissionModel(
    val g2f:Boolean,
    val otp: Boolean
){
    init {
        updateAppPermissions()
    }
    private fun updateAppPermissions(){
        Log.i("Log1","----- Login data updated -----")
        LoginData.permissionModel = this
    }

    fun needPermission():Boolean{
        updateAppPermissions()
        return g2f or otp
    }
}
