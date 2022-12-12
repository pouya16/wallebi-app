package com.example.wallebi_app.database.models

import com.example.wallebi_app.api.reg.model.PermissionModel
import com.example.wallebi_app.api.setting.KycLevelModel
import com.example.wallebi_app.api.setting.UserLevelModel

data class MeModel(
    val first_name:String,
    val last_name:String,
    val email:String,
    val full_email:String,
    val is_withdraw_whitelist:Boolean,
    val permission:PermissionModel,
    val user_level:UserLevelModel,
    val kyc_level:KycLevelModel,
    val is_password:Boolean,
    val phone_number:String,
    val uid:String,
    val bankAccount_No:Int,
    val full_phone_number:String,
    val pnl_status:Int
)