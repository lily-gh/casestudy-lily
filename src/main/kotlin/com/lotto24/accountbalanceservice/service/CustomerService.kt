package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.dto.BookMoneyResponse
import com.lotto24.accountbalanceservice.dto.VoidTransactionResponse
import com.lotto24.accountbalanceservice.exception.AmountCannotBeZeroException
import com.lotto24.accountbalanceservice.exception.CustomerNotFoundException
import com.lotto24.accountbalanceservice.exception.NotEnoughFundsException
import com.lotto24.accountbalanceservice.model.Customer
import com.lotto24.accountbalanceservice.model.CustomerBalance
import com.lotto24.accountbalanceservice.repository.CustomerBalanceRepository
import com.lotto24.accountbalanceservice.repository.CustomerRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val customerBalanceRepository: CustomerBalanceRepository,
    val transactionService: TransactionService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun bookMoney(tenantId: Int, customerExternalId: Int, amount: BigDecimal): BookMoneyResponse {
        val customer = getCustomer(tenantId, customerExternalId)
        val currentBalance = getCurrentBalance(customer)

        if (isZero(amount)) throw AmountCannotBeZeroException("Money amount can not be Zero.")
        if (amount <= BigDecimal.ZERO && amount.abs() >= currentBalance.balance) throw NotEnoughFundsException("Not enough funds to pay out. Current balance: ${currentBalance.balance}")

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
            ?: throw CustomerNotFoundException("Customer not found with customerNumber $customerExternalId for tenant $tenantId")
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

        logger.debug("Updating customerBalance for customerId {}", updatedBalance.customerId)
        return customerBalanceRepository.save(updatedBalance)
    }

    private fun isZero(amount: BigDecimal): Boolean {
        return amount == BigDecimal.ZERO || amount.toDouble() == 0.0
    }
}
