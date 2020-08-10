package com.saifu.saifu_v3.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import oauth.signpost.OAuthConsumer
import oauth.signpost.OAuthProvider
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode

@Configuration
class BeanConfig {

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun oAuthConsumer(oAuthConfig: OAuthConfig): OAuthConsumer {
        return CommonsHttpOAuthConsumer(oAuthConfig.CONSUMER_KEY, oAuthConfig.CONSUMER_SECRET)
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun oAuthProvider(oAuthConfig: OAuthConfig): OAuthProvider {
        return CommonsHttpOAuthProvider(oAuthConfig.REQUEST_TOKEN_URL, oAuthConfig.ACCESS_TOKEN_URL, oAuthConfig.AUTHORIZE_URL)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return mapper
    }

}
