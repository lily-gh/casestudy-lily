package com.lotto24.accountbalanceservice.dto

import com.lotto24.accountbalanceservice.config.AuditableInterceptor

data class AuditPayload(
    private val payload: Map<Type, String> = mapOf(),
    private val pPayload: MutableMap<Type, String> = mutableMapOf(),
) {

    init {
        pPayload.putAll(payload)
    }

    enum class Type(val label: String) {
        TENANT_ID(AuditableInterceptor.TENANT_ID),
        CUSTOMER_NUMBER(AuditableInterceptor.CUSTOMER_NUMBER),
        CUSTOMER_ID(AuditableInterceptor.CUSTOMER_ID),
        TRANSACTION_ID(AuditableInterceptor.TRANSACTION_ID),
    }

    fun add(key: Type, value: String) {
        pPayload[key] = value
    }

    fun get(key: Type) = pPayload[key]
}
