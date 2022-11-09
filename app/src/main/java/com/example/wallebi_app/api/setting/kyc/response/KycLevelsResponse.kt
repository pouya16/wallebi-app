package com.example.wallebi_app.api.setting.kyc.response

import com.example.wallebi_app.api.setting.kyc.models.KycLevelsModel

data class KycLevelsResponse(
    val success:Boolean,
    val data:List<KycLevelsModel>,
    val err:String
)