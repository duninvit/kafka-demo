package com.github.duninvit.configserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer


@Configuration
class SecurityConfiguration {

    @Bean
    fun ignoringCustomizer() = WebSecurityCustomizer { web: WebSecurity ->
        web.ignoring().antMatchers(
            "/actuator/**",
            "/encrypt/**",
            "/decrypt/**"
        )
    }
}
