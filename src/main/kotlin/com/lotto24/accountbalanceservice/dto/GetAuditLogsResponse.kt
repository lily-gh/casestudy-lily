package com.lotto24.accountbalanceservice.dto

import com.lotto24.accountbalanceservice.model.AuditLog

data class GetAuditLogsResponse(
    val auditLogs: List<AuditLog>,
    val page: Int,
    val totalElements: Long,
)
