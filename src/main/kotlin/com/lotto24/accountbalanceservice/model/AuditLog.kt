package com.lotto24.accountbalanceservice.model

import com.lotto24.accountbalanceservice.config.AuditType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "audit_log")
data class AuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    val type: AuditType,
    val tenantId: Int,
    val externalId: Int,
    val customerId: Int,
    val transactionId: Int,
    val remoteAddress: String,
    val createdAt: LocalDateTime,
)
