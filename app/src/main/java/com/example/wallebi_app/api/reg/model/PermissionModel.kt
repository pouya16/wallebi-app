package com.example.wallebi_app.api.reg.model
import com.example.wallebi_app.database.LoginData

class PermissionModel(
    val g2f:Boolean,
    val otp: Boolean
){
    init {
        updateAppPermissions()
    }
    private fun updateAppPermissions(){
        LoginData.permissionModel = this
    }
}
