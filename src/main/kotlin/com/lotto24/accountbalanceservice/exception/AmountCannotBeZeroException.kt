package com.lotto24.accountbalanceservice.exception

data class AmountCannotBeZeroException(override val message: String) : RuntimeException(message)
