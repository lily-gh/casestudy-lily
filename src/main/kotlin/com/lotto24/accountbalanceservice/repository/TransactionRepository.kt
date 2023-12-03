package com.lotto24.accountbalanceservice.repository

import com.lotto24.accountbalanceservice.model.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Int> {
    fun findByIdAndCustomerId(transactionId: Int, customerId: Int): Transaction?
    fun findAllByCustomerId(customerId: Int, pageable: Pageable): Page<Transaction>
}
