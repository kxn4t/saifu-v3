package com.saifu.saifu_v3.service

import com.saifu.saifu_v3.model.Money
import org.springframework.stereotype.Service

@Service
class MoneyService(
        private val zaimClient: ZaimClient
) {

    fun getAllMoney(): List<Money> = zaimClient.getAllMoney()

}
