package com.lotto24.accountbalanceservice.repository

import com.lotto24.accountbalanceservice.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Int> {
    fun findByTenantIdAndExternalId(tenantId: Int, externalId: Int): Customer?
}
