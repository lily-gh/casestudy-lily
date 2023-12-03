package com.lotto24.accountbalanceservice.controller

import com.lotto24.accountbalanceservice.dto.GetRecentTransactionsResponse
import com.lotto24.accountbalanceservice.model.Transaction
import com.lotto24.accountbalanceservice.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customercare")
class CustomerCareController(
    val customerService: CustomerService,
) {
    @GetMapping("/tenants/{tenantId}/customers/{customerNumber}/transactions", produces = ["application/json"])
    fun getRecentTransactions(
        @PathVariable tenantId: Int,
        @PathVariable customerNumber: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): GetRecentTransactionsResponse {
        val pageable = PageRequest.of(page, size)
        return customerService.getRecentTransactions(
            tenantId = tenantId,
            customerExternalId = customerNumber,
            pageable = pageable,
        )
    }
}
