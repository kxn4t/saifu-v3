package com.saifu.saifu_v3.model

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
class UserDetails {
    var accessToken: String = ""
    var accessTokenSecret: String = ""
}
