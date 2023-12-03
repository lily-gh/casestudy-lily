package com.lotto24.accountbalanceservice.repository

import com.lotto24.accountbalanceservice.model.CustomerBalance
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerBalanceRepository : CrudRepository<CustomerBalance, Int> {
    fun findByCustomerId(customerId: Int): CustomerBalance?
}
