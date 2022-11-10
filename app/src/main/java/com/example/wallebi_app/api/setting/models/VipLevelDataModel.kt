package com.example.wallebi_app.api.setting.models

data class VipLevelDataModel(
    val user_vip: UserVipModel,
    val vip_levels: List<VipLevelsModel>
)