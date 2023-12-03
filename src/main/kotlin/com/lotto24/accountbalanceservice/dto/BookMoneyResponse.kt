package com.lotto24.accountbalanceservice.dto

import java.math.BigDecimal

data class BookMoneyResponse(
    val transactionId: Int,
    val balance: BigDecimal,
)
