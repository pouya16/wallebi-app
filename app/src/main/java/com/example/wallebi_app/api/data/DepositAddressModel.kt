package com.example.wallebi_app.api.data

import java.math.BigInteger

data class DepositAddressModel(
    val address: String,
    val fee:Double,
    val memo:BigInteger
)