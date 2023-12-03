package com.lotto24.accountbalanceservice.service

import com.lotto24.accountbalanceservice.model.Transaction
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_IN
import com.lotto24.accountbalanceservice.model.TransactionType.PAY_OUT
import com.lotto24.accountbalanceservice.model.TransactionType.VOIDED
import com.lotto24.accountbalanceservice.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    val transactionRepository: TransactionRepository,
) {

    fun saveTransaction(customerId: Int, amount: BigDecimal): Transaction {
        // TODO: validate balance amount and/with current balance
        val transactionType = if (amount <= BigDecimal.ZERO) PAY_OUT else PAY_IN
        val transaction = Transaction(
            customerId = customerId,
            amount = amount,
            type = transactionType,
            createdAt = LocalDateTime.now(),
        )

        return transactionRepository.save(transaction)
    }

    fun voidTransaction(transactionId: Int, customerId: Int): Transaction {
        val transaction = transactionRepository.findByIdAndCustomerId(transactionId, customerId)
            ?: throw IllegalArgumentException("Transaction $transactionId not found for customerId $customerId")

        if (transaction.type == VOIDED) throw java.lang.IllegalArgumentException("Transaction $transactionId is already voided.")

        return transactionRepository.save(transaction.copy(type = VOIDED, updatedAt = LocalDateTime.now()))
    }
}
