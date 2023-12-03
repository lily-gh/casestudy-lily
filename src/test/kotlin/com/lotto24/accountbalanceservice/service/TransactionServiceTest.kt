package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.exception.TransactionAlreadyVoidedException
import com.lotto24.accountbalanceservice.exception.TransactionNotFoundException
import com.lotto24.accountbalanceservice.model.Transaction
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_IN
import com.lotto24.accountbalanceservice.model.TransactionType.VOIDED
import com.lotto24.accountbalanceservice.repository.TransactionRepository
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime

internal class TransactionServiceTest {

    private val transactionRepository: TransactionRepository = mockk()

    private val transactionService = TransactionService(transactionRepository)

    @Test
    fun `should save a transaction`() {
        // GIVEN
        val customerId = 1
        val amount = BigDecimal(100.00)
        val expectedSavedTransaction = Transaction(
            id = 1,
            customerId = customerId,
            amount = amount,
            type = PAY_IN,
            createdAt = LocalDateTime.now(),
        )
        every { transactionRepository.save(any()) } returns expectedSavedTransaction

        // WHEN
        val savedTransaction = transactionService.saveTransaction(1, BigDecimal(100.00))

        // THEN
        assertNotNull(savedTransaction.id)
        assertNotNull(savedTransaction.createdAt)
        verify { transactionRepository.save(any()) }
    }

    @Test
    fun `should void an existing transaction`() {
        // GIVEN
        val customerId = 1
        val amount = BigDecimal(100.00)
        val transactionId = 23
        val existingTransaction = Transaction(
            id = transactionId,
            customerId = customerId,
            amount = amount,
            type = PAY_IN,
            createdAt = LocalDateTime.now(),
        )
        every { transactionRepository.findByIdAndCustomerId(transactionId, customerId) } returns existingTransaction
        every { transactionRepository.save(any()) } returns mockk()

        // WHEN
        transactionService.voidTransaction(transactionId, customerId)

        // THEN
        verify {
            transactionRepository.save(
                withArg { transaction ->
                    assertTrue(transaction.type == VOIDED)
                    assertNotNull(transaction.updatedAt)
                },
            )
        }
    }

    @Test
    fun `should fail to void an already voided transaction`() {
        // GIVEN
        val customerId = 1
        val amount = BigDecimal(100.00)
        val transactionId = 23
        val existingTransaction = Transaction(
            id = transactionId,
            customerId = customerId,
            amount = amount,
            type = VOIDED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
        every { transactionRepository.findByIdAndCustomerId(transactionId, customerId) } returns existingTransaction

        // WHEN
        assertThrows<TransactionAlreadyVoidedException> {
            transactionService.voidTransaction(transactionId, customerId)
        }

        // THEN
        verify { transactionRepository.save(any()) wasNot Called }
    }

    @Test
    fun `should fail to void a transaction if it is not found`() {
        // GIVEN
        val customerId = 1
        val amount = BigDecimal(100.00)
        val transactionId = 23
        every { transactionRepository.findByIdAndCustomerId(transactionId, customerId) } returns null

        // WHEN
        assertThrows<TransactionNotFoundException> {
            transactionService.voidTransaction(transactionId, customerId)
        }

        // THEN
        verify { transactionRepository.save(any()) wasNot Called }
    }
}
