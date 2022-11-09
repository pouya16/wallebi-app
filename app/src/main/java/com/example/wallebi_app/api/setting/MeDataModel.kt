package com.example.wallebi_app.api.setting

import android.security.keystore.KeyNotYetValidException
import com.example.wallebi_app.api.reg.model.PermissionModel

data class MeDataModel(
    val first_name:String,
    val last_name:String,
    val email: String,
    val full_email:String,
    val is_withdrawal_whitelist:Boolean,
    val permission: PermissionModel,
    val user_level:UserLevelModel,
    val kyc_level:KycLevelModel,
    val is_password:Boolean,
    val phone_number:String,
    val uid:String,
    val bankAccount_No:Int,
    val full_phone_number:String,
    val pnl_status:Int
)
