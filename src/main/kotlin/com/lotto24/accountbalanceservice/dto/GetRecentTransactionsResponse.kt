package com.lotto24.accountbalanceservice.dto

import com.lotto24.accountbalanceservice.model.Transaction

data class GetRecentTransactionsResponse(
    val transactions: List<Transaction>,
    val page: Int,
    val totalElements: Long,
)
