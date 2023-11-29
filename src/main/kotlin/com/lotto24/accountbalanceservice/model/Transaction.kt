package com.lotto24.accountbalanceservice.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val customerId: Int,
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)

enum class TransactionType {
    PAY_IN, PAY_OUT, VOIDED
}
