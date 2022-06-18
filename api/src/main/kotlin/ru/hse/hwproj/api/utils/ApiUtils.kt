package ru.hse.hwproj.api.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T : Any> T?.orElseStatus(status: HttpStatus): ResponseEntity<T> =
    this?.let {
        ResponseEntity(it, HttpStatus.OK)
    } ?: ResponseEntity(status)

fun <T : Any> T?.runOrElseStatus(status: HttpStatus, block: (T) -> Unit): ResponseEntity<T> =
    this?.let {
        block(it)
        ResponseEntity(it, HttpStatus.OK)
    } ?: ResponseEntity(status)
