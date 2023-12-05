package com.lotto24.accountbalanceservice.controller

import com.lotto24.accountbalanceservice.dto.GetAuditLogsResponse
import com.lotto24.accountbalanceservice.service.AuditLogService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auditlogs")
class AuditLogController(
    val auditLogService: AuditLogService,
) {
    @GetMapping(produces = ["application/json"])
    fun getAuditLogs(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): GetAuditLogsResponse {
        val pageable = PageRequest.of(page, size)
        return auditLogService.getAuditLogs(
            pageable = pageable,
        )
    }
}
