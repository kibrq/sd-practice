package ru.hse.hwproj.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/*
 * REST-controller for handling a simple health request.
 */
@RestController
class HealthController {
    @GetMapping("/api/health")
    fun health() = "health ok"
}
