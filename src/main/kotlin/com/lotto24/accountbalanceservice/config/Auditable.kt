package com.lotto24.accountbalanceservice.config

@Target(AnnotationTarget.FUNCTION)
annotation class Auditable(
    val type: AuditType,
)

enum class AuditType {
    MONEY_BOOKING,
    TRANSACTION_VOIDED,
}
