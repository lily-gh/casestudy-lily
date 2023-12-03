package com.lotto24.accountbalanceservice.controller

import com.lotto24.accountbalanceservice.dto.BookMoneyRequest
import com.lotto24.accountbalanceservice.dto.BookMoneyResponse
import com.lotto24.accountbalanceservice.dto.VoidTransactionRequest
import com.lotto24.accountbalanceservice.dto.VoidTransactionResponse
import com.lotto24.accountbalanceservice.service.CustomerService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("tenants")
class TenantController(
    val customerService: CustomerService,
) {
    @PostMapping("/{tenantId}/customers/{customerNumber}/book-money", produces = ["application/json"])
    fun bookMoney(
        @PathVariable tenantId: Int,
        @PathVariable customerNumber: Int,
        @RequestBody request: BookMoneyRequest,
    ): BookMoneyResponse {
        return customerService.bookMoney(
            tenantId = tenantId,
            customerExternalId = customerNumber,
            amount = request.amount,
        )
    }

    @PatchMapping("/{tenantId}/customers/{customerNumber}/void-transaction", produces = ["application/json"])
    fun voidTransaction(
        @PathVariable tenantId: Int,
        @PathVariable customerNumber: Int,
        @RequestBody request: VoidTransactionRequest,
    ): VoidTransactionResponse {
        return customerService.voidTransaction(
            tenantId = tenantId,
            customerExternalId = customerNumber,
            transactionId = request.transactionId,
        )
    }
}
