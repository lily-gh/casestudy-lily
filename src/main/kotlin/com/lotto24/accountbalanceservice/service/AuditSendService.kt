package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.config.AuditType
import com.lotto24.accountbalanceservice.dto.AuditPayload
import com.lotto24.accountbalanceservice.model.AuditLog
import com.lotto24.accountbalanceservice.repository.AuditLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuditSendService(
    val auditLogRepository: AuditLogRepository,
) {

    fun send(auditType: AuditType, payload: AuditPayload, remoteAddr: String) {
        val tenantId = payload.get(AuditPayload.Type.TENANT_ID)!!.toInt()
        val externalId = payload.get(AuditPayload.Type.CUSTOMER_NUMBER)!!.toInt()
        val customerId = payload.get(AuditPayload.Type.CUSTOMER_ID)!!.toInt()
        val transactionId = payload.get(AuditPayload.Type.TRANSACTION_ID)!!.toInt()
        val auditLog = AuditLog(
            type = auditType,
            tenantId = tenantId,
            externalId = externalId,
            customerId = customerId,
            transactionId = transactionId,
            remoteAddress = remoteAddr,
            createdAt = LocalDateTime.now(),
        )

        auditLogRepository.save(auditLog)
    }
}
