package com.saifu.saifu_v3.controller

import com.saifu.saifu_v3.model.Money
import com.saifu.saifu_v3.service.MoneyService
import org.springframework.web.bind.annotation.*

@RestController("/api")
class MoneyAPIController(
        private val moneyService: MoneyService
) {

    @GetMapping("/money/{moneyId}")
    fun getMoney(@PathVariable("moneyId") moneyId: String): Money? {
        return moneyService.fetchMoneyById(moneyId)
    }

    @PutMapping("/money/{moneyId}/update")
    fun updateMoney(@RequestBody money: Money, @PathVariable("moneyId") moneyId: String): Money? {
        if (money.id != moneyId) {
            throw IllegalArgumentException("ID不一致")
        }
        if (!moneyService.updateMoney(money)) {
            throw IllegalStateException("更新に失敗")
        }
        return moneyService.fetchMoneyById(money.id)
    }

}
