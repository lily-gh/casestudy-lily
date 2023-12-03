package com.lotto24.accountbalanceservice.exception

data class CustomerNotFoundException(override val message: String) : RuntimeException(message)
