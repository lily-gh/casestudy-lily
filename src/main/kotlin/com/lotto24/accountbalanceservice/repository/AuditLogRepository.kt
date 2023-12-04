package com.lotto24.accountbalanceservice.repository

import com.lotto24.accountbalanceservice.model.AuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuditLogRepository : JpaRepository<AuditLog, Int>
