package com.lotto24.accountbalanceservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "customer_balance")
data class CustomerBalance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val customerId: Int,
    val balance: BigDecimal,
    val updatedAt: LocalDateTime,
)
