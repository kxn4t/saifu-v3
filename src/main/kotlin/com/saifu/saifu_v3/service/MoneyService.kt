package com.saifu.saifu_v3.service

import com.saifu.saifu_v3.model.Money
import com.saifu.saifu_v3.type.MoneyConditionType
import org.springframework.stereotype.Service

@Service
class MoneyService(
        private val zaimClient: ZaimClient
) {

    fun getAllMoneyList(): List<Money> = zaimClient.getMoneyList()

    fun searchMoneyList(searchParameters: Map<MoneyConditionType, String> = mapOf()): List<Money> {
        return zaimClient.getMoneyList(searchParameters)
    }

}
