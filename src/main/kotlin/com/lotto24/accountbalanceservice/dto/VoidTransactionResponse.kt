package com.lotto24.accountbalanceservice.dto

import java.math.BigDecimal

data class VoidTransactionResponse(
    val balance: BigDecimal,
)
