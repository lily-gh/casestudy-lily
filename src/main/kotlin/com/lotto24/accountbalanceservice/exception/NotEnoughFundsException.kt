package com.lotto24.accountbalanceservice.exception

data class NotEnoughFundsException(override val message: String) : RuntimeException(message)
