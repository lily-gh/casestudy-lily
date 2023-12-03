package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.model.Customer
import com.lotto24.accountbalanceservice.model.CustomerBalance
import com.lotto24.accountbalanceservice.model.Transaction
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_IN
import com.lotto24.accountbalanceservice.model.TransactionType.VOIDED
import com.lotto24.accountbalanceservice.repository.CustomerBalanceRepository
import com.lotto24.accountbalanceservice.repository.CustomerRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal
import java.time.LocalDateTime

internal class CustomerServiceTest {

    private val customerRepository: CustomerRepository = mockk()
    private val customerBalanceRepository: CustomerBalanceRepository = mockk()
    private val transactionService: TransactionService = mockk()

    private val customerService = CustomerService(
        customerRepository,
        customerBalanceRepository,
        transactionService,
    )

    companion object {
        val SAVED_CUSTOMER = Customer(
            id = 1,
            tenantId = 1,
            externalId = 100,
            createdAt = LocalDateTime.now(),
        )
        val CURRENT_BALANCE = CustomerBalance(customerId = 1, balance = BigDecimal(200.00), createdAt = LocalDateTime.now())
    }

    @BeforeEach
    fun beforeEach() {
        every { customerRepository.findByTenantIdAndExternalId(any(), any()) } returns SAVED_CUSTOMER
        every { customerBalanceRepository.findByCustomerId(customerId = 1) } returns CURRENT_BALANCE
    }

    @ParameterizedTest
    @ValueSource(doubles = [300.00, -50.00])
    fun `should book money with positive or negative amounts`(value: Double) {
        // GIVEN
        val tenantId = 1
        val customerExternalId = 100
        val amount = BigDecimal(value)
        every { transactionService.saveTransaction(any(), any()) } returns Transaction(
            id = 42,
            customerId = 1,
            amount = amount,
            type = PAY_IN,
            createdAt = LocalDateTime.now(),
        )

        val updatedAmount = CURRENT_BALANCE.balance.plus(amount)
        val updatedBalance = CURRENT_BALANCE.copy(balance = updatedAmount, updatedAt = LocalDateTime.now())
        every { customerBalanceRepository.save(any()) } returns updatedBalance

        // WHEN
        val bookMoneyResponse = customerService.bookMoney(tenantId, customerExternalId, amount)

        // THEN
        assertEquals(updatedAmount, bookMoneyResponse.balance)
        assertEquals(42, bookMoneyResponse.transactionId)
        verify { transactionService.saveTransaction(any(), any()) }
        verify {
            customerBalanceRepository.save(
                withArg {
                    assertTrue(it.balance == updatedAmount)
                    assertNotNull(it.updatedAt)
                },
            )
        }
    }

    @Test
    fun `should void an existing transaction`() {
        // GIVEN
        val tenantId = 1
        val customerExternalId = 100

        val amount = BigDecimal(100.00)
        val transactionId = 23
        val customerId = 1
        val voidedTransaction = Transaction(
            id = transactionId,
            customerId = customerId,
            amount = amount,
            type = VOIDED,
            createdAt = LocalDateTime.now(),
        )
        every { transactionService.voidTransaction(any(), any()) } returns voidedTransaction

        val updatedAmount = CURRENT_BALANCE.balance.plus(amount.negate())
        val updatedBalance = CURRENT_BALANCE.copy(balance = updatedAmount, updatedAt = LocalDateTime.now())
        every { customerBalanceRepository.save(any()) } returns updatedBalance

        // WHEN
        val voidedTransactionResponse = customerService.voidTransaction(tenantId, customerExternalId, transactionId)

        // THEN
        verify {
            customerBalanceRepository.save(
                withArg {
                    assertEquals(updatedAmount, it.balance)
                    assertNotNull(it.updatedAt)
                },
            )
        }
        assertEquals(updatedAmount, voidedTransactionResponse.balance)
    }
}
