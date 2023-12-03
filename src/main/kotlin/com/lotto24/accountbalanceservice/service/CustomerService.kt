package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.dto.BookMoneyResponse
import com.lotto24.accountbalanceservice.dto.VoidTransactionResponse
import com.lotto24.accountbalanceservice.model.Customer
import com.lotto24.accountbalanceservice.model.CustomerBalance
import com.lotto24.accountbalanceservice.repository.CustomerBalanceRepository
import com.lotto24.accountbalanceservice.repository.CustomerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val customerBalanceRepository: CustomerBalanceRepository,
    val transactionService: TransactionService,
) {

    @Transactional
    fun bookMoney(tenantId: Int, customerExternalId: Int, amount: BigDecimal): BookMoneyResponse {
        // TODO: use custom exceptions
        val customer = getCustomer(tenantId, customerExternalId)
        val currentBalance = getCurrentBalance(customer)

        if (isZero(amount)) throw IllegalArgumentException("Money amount can not be Zero.")
        if (amount <= BigDecimal.ZERO && amount >= currentBalance.balance) throw IllegalArgumentException("Not enough funds to pay out.")

        val transaction = transactionService.saveTransaction(customer.id, amount)
        val updatedBalance = updateCustomerBalance(currentBalance, amount)

        return BookMoneyResponse(transaction.id!!, updatedBalance.balance)
    }

    @Transactional
    fun voidTransaction(tenantId: Int, customerExternalId: Int, transactionId: Int): VoidTransactionResponse {
        val customer = getCustomer(tenantId, customerExternalId)
        val currentBalance = getCurrentBalance(customer)

        val transaction = transactionService.voidTransaction(transactionId = transactionId, customerId = customer.id)
        val updatedBalance = updateCustomerBalance(currentBalance, transaction.amount.negate())
        return VoidTransactionResponse(updatedBalance.balance)
    }

    private fun getCustomer(tenantId: Int, customerExternalId: Int): Customer {
        return customerRepository.findByTenantIdAndExternalId(tenantId, customerExternalId)
            ?: throw IllegalArgumentException("Customer not found with customerNumber $customerExternalId for tenant $tenantId")
    }

    private fun getCurrentBalance(customer: Customer): CustomerBalance {
        return customerBalanceRepository.findByCustomerId(customer.id)
            ?: CustomerBalance(customerId = customer.id, balance = BigDecimal.ZERO, createdAt = LocalDateTime.now())
    }

    private fun updateCustomerBalance(currentBalance: CustomerBalance, amount: BigDecimal): CustomerBalance {
        val updatedBalance = currentBalance.copy(
            balance = currentBalance.balance.plus(amount),
            updatedAt = LocalDateTime.now(),
        )

        return customerBalanceRepository.save(updatedBalance)
    }

    private fun isZero(amount: BigDecimal): Boolean {
        return amount == BigDecimal.ZERO || amount.toDouble() == 0.0
    }
}
