package com.lotto24.accountbalanceservice.config

import com.lotto24.accountbalanceservice.dto.AuditPayload
import com.lotto24.accountbalanceservice.service.AuditSendService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.ModelAndView

@Component
class AuditableInterceptor(val auditSendService: AuditSendService) : HandlerInterceptor {

    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        const val TENANT_ID = "tenantId"
        const val CUSTOMER_NUMBER = "customerNumber"
        const val CUSTOMER_ID = "customerId"
        const val TRANSACTION_ID = "transactionId"
        const val AUDIT_PAYLOAD = "auditPayload"

        val paramsToSearch =
            listOf(TENANT_ID, CUSTOMER_NUMBER)
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val annotation = handler.getMethodAnnotation(Auditable::class.java)
            if (annotation != null) {
                val payload = AuditPayload()
                request.setAttribute(AUDIT_PAYLOAD, payload)
                populatePayloadByParam(request, payload)
            }
        }
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?,
    ) {
        if (handler is HandlerMethod) {
            val annotation = handler.getMethodAnnotation(Auditable::class.java)

            if (annotation != null) {
                val payload = request.getAttribute(AUDIT_PAYLOAD) as AuditPayload

                auditSendService.send(
                    annotation.type,
                    payload,
                    request.remoteAddr,
                )
            } else {
                logger.warn("Not sending audit - response code is ${response.status}")
            }
        }
    }

    private fun populatePayloadByParam(
        request: HttpServletRequest,
        payload: AuditPayload,
    ) {
        val paths =
            request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>

        paramsToSearch.forEach { param ->
            getValueFromParamOrPath(paths, param, request)?.let { value ->
                payload.add(AuditPayload.Type.values().first { it.label == param }, value)
            }
        }
    }

    private fun getValueFromParamOrPath(paths: Map<String, String>, paramName: String, request: HttpServletRequest): String? =
        if (paths[paramName] != null) {
            paths[paramName]
        } else {
            request.parameterMap[paramName]?.let { orgParams ->
                orgParams[0] // Not handled: duplicated param values
            }
        }
}
