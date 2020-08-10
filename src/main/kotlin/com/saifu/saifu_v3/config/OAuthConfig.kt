package com.saifu.saifu_v3.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "zaim")
data class OAuthConfig(
        val CONSUMER_KEY: String,
        val CONSUMER_SECRET: String,
        val REQUEST_TOKEN_URL: String,
        val AUTHORIZE_URL: String,
        val ACCESS_TOKEN_URL: String,
        val CALLBACK_URL: String
)
