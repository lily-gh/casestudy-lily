package com.lotto24.accountbalanceservice.exception

import com.lotto24.accountbalanceservice.dto.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleCustomerNotFoundException(ex: CustomerNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("CustomerNotFoundException happened: {}", ex.message)
        val errorResponse = ErrorResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleAmountCannotBeZeroException(ex: AmountCannotBeZeroException): ResponseEntity<ErrorResponse> {
        logger.info("AmountCannotBeZeroException happened: {}", ex.message)
        val errorResponse = ErrorResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleNotEnoughFundsException(ex: NotEnoughFundsException): ResponseEntity<ErrorResponse> {
        logger.info("NotEnoughFundsException happened: {}", ex.message)
        val errorResponse = ErrorResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler
    fun handleTransactionAlreadyVoidedException(ex: TransactionAlreadyVoidedException): ResponseEntity<ErrorResponse> {
        logger.info("TransactionAlreadyVoidedException happened: {}", ex.message)
        val errorResponse = ErrorResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleTransactionNotFoundException(ex: TransactionNotFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("TransactionNotFoundException happened: {}", ex.message)
        val errorResponse = ErrorResponse(ex.message)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}
