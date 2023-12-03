package com.lotto24.accountbalanceservice.dto

data class GetRecentTransactionsRequest(
    val tenantId: Int,
    val customerNumber: Int,
)
