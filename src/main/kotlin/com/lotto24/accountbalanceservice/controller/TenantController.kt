package com.lotto24.accountbalanceservice.controller

import com.lotto24.accountbalanceservice.config.AuditType
import com.lotto24.accountbalanceservice.config.Auditable
import com.lotto24.accountbalanceservice.config.AuditableInterceptor.Companion.AUDIT_PAYLOAD
import com.lotto24.accountbalanceservice.dto.AuditPayload
import com.lotto24.accountbalanceservice.dto.BookMoneyRequest
import com.lotto24.accountbalanceservice.dto.BookMoneyResponse
import com.lotto24.accountbalanceservice.dto.VoidTransactionRequest
import com.lotto24.accountbalanceservice.dto.VoidTransactionResponse
import com.lotto24.accountbalanceservice.service.CustomerService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("tenants")
class TenantController(
    val customerService: CustomerService,
) {
    @PostMapping("/{tenantId}/customers/{customerNumber}/book-money", produces = ["application/json"])
    @Auditable(type = AuditType.MONEY_BOOKING)
    fun bookMoney(
        @PathVariable tenantId: Int,
        @PathVariable customerNumber: Int,
        @RequestBody request: BookMoneyRequest,
        @RequestAttribute(AUDIT_PAYLOAD) auditPayload: AuditPayload,
    ): BookMoneyResponse {
        return customerService.bookMoney(
            tenantId = tenantId,
            customerExternalId = customerNumber,
            amount = request.amount,
            auditPayload = auditPayload,
        )
    }

    @PatchMapping("/{tenantId}/customers/{customerNumber}/void-transaction", produces = ["application/json"])
    @Auditable(type = AuditType.TRANSACTION_VOIDED)
    fun voidTransaction(
        @PathVariable tenantId: Int,
        @PathVariable customerNumber: Int,
        @RequestBody request: VoidTransactionRequest,
        @RequestAttribute(AUDIT_PAYLOAD) auditPayload: AuditPayload,
    ): VoidTransactionResponse {
        auditPayload.add(AuditPayload.Type.TRANSACTION_ID, request.transactionId.toString())

        return customerService.voidTransaction(
            tenantId = tenantId,
            customerExternalId = customerNumber,
            transactionId = request.transactionId,
            auditPayload = auditPayload,
        )
    }
}
