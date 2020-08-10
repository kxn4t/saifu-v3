package com.saifu.saifu_v3.controller

import com.saifu.saifu_v3.model.UserDetails
import com.saifu.saifu_v3.service.ZaimClient
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class LoginController(
        private val userDetails: UserDetails,
        private val zaimClient: ZaimClient
) {

    /**
     * step 1 : move to zaim with requestToken
     */
    @GetMapping("/login")
    fun login(): String {
        return if (userDetails.accessToken.isNotEmpty()) {
            // already login
            "redirect:/"
        } else {
            // move to zaim for authentication
            "redirect:" + zaimClient.retrieveRequestToken()
        }
    }

    /**
     * step 2 : callback from zaim
     */
    @GetMapping("/login/callback")
    fun loginCallback(@RequestParam(value = "oauth_verifier") oauthVerifier: String): String {
        return if (userDetails.accessToken.isNotEmpty()) {
            // already login
            "redirect:/"
        } else {
            // generate tokens
            zaimClient.retrieveAccessToken(oauthVerifier)
            "redirect:/"
        }
    }

}
