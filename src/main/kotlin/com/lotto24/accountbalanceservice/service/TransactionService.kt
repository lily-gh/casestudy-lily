package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.exception.TransactionAlreadyVoidedException
import com.lotto24.accountbalanceservice.exception.TransactionNotFoundException
import com.lotto24.accountbalanceservice.model.Transaction
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_IN
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_OUT
import com.lotto24.accountbalanceservice.model.TransactionType.VOIDED
import com.lotto24.accountbalanceservice.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    val transactionRepository: TransactionRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun saveTransaction(customerId: Int, amount: BigDecimal): Transaction {
        val transactionType = if (amount <= BigDecimal.ZERO) PAY_OUT else PAY_IN
        val transaction = Transaction(
            customerId = customerId,
            amount = amount,
            type = transactionType,
            createdAt = LocalDateTime.now(),
        )
        logger.debug("Saving new transaction for customerId {} with amount {}", customerId, amount)
        return transactionRepository.save(transaction)
    }

    fun voidTransaction(transactionId: Int, customerId: Int): Transaction {
        val transaction = transactionRepository.findByIdAndCustomerId(transactionId, customerId)
            ?: throw TransactionNotFoundException("Transaction $transactionId not found for customerId $customerId")

        if (transaction.type == VOIDED) throw TransactionAlreadyVoidedException("Transaction $transactionId is already voided.")

        logger.debug("Voiding transaction {} for customerId {}", transactionId, customerId)
        return transactionRepository.save(transaction.copy(type = VOIDED, updatedAt = LocalDateTime.now()))
    }

    fun getTransactions(customerId: Int, pageable: Pageable): Page<Transaction> {
        return transactionRepository.findAllByCustomerId(customerId, pageable)
    }
}
