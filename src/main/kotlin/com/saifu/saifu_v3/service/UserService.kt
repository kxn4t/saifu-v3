package com.saifu.saifu_v3.service

import com.saifu.saifu_v3.model.User
import org.springframework.stereotype.Service

@Service
class UserService(
        private val zaimClient: ZaimClient
) {

    fun getUser(): User = zaimClient.fetchMe()

}
