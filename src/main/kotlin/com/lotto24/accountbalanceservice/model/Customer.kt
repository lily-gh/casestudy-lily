package com.lotto24.accountbalanceservice.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "customer")
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    val tenantId: Int,
    val externalId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
)
