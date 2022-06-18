package ru.hse.hwproj.api.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/*
 * Hwproj web security configuration.
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    private fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val origin = System.getenv("SITE_URL") ?: "http://localhost:3000"
        configuration.allowedOrigins = listOf(origin)
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("authorization", "content-type", "x-auth-token")
        configuration.exposedHeaders = listOf("x-auth-token", "set-cookie")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            cors {
                configurationSource = corsConfigurationSource()
            }
        }
        return http.build()
    }
}
