package com.lotto24.accountbalanceservice.exception

data class TransactionNotFoundException(override val message: String) : RuntimeException(message)
