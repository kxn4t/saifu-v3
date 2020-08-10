package com.saifu.saifu_v3.service

import com.saifu.saifu_v3.model.Money
import com.saifu.saifu_v3.type.MoneyConditionType
import org.springframework.stereotype.Service

@Service
class MoneyService(
        private val zaimClient: ZaimClient
) {

    fun findAllMoneyList(): List<Money> = zaimClient.getMoneyList()

    fun findMoneyList(searchParameters: Map<MoneyConditionType, String> = mapOf()): List<Money> {
        return zaimClient.getMoneyList(searchParameters)
    }

}
