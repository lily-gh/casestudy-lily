package com.lotto24.accountbalanceservice.exception

data class TransactionAlreadyVoidedException(override val message: String) : RuntimeException(message)
