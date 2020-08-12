package com.saifu.saifu_v3.service

import com.saifu.saifu_v3.form.CalculateForm
import com.saifu.saifu_v3.model.CalculateResult
import com.saifu.saifu_v3.model.CalculateResultByKeyword
import com.saifu.saifu_v3.model.Money
import com.saifu.saifu_v3.type.MoneyConditionType
import com.saifu.saifu_v3.utils.Utils
import com.saifu.saifu_v3.utils.between
import com.saifu.saifu_v3.utils.toLocalDate
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MoneyService(
        private val zaimClient: ZaimClient
) {

    fun fetchMoneyList(searchParameters: Map<MoneyConditionType, String> = mapOf()): List<Money> {
        return zaimClient.getMoneyList(searchParameters)
    }

    fun calculate(condition: CalculateForm): CalculateResult {

        val fromDate = condition.from.toLocalDate(Utils.dateTimeFormatter)
        val toDate = condition.to.toLocalDate(Utils.dateTimeFormatter)

        // 期間内のMoneyListを取得
        val calculateSource = fetchSixMonthsAgoMoneyList(fromDate).filter {
            it.created.toLocalDate(Utils.zaimDateTimeFormatter).between(fromDate, toDate)
        }

        // 合計額
        val total = calculateSource.sumBy { it.amount }

        // キーワード毎に合計する
        val sumResults = mutableMapOf<String, Int>()
        condition.keyword.forEach { keyword ->
            sumResults[keyword] = calculateSource.filter { it.comment == keyword }.sumBy { it.amount }
        }

        // キーワードに一致しないものをチェックする
        val wrongList = calculateSource.filter {
            !condition.keyword.contains(it.comment)
        }
        val wrongSum = wrongList.sumBy { it.amount }

        // 合計額が一致しないとき
        if (total != sumResults.map { it.value }.sum() + wrongSum) {
            throw ArithmeticException("合計金額とそれぞれの小計が一致しません")
        }

        // 差分計算して結果を格納
        val average = sumResults.map { it.value }.sum() / condition.keyword.size
        val resultByKeyword = mutableListOf<CalculateResultByKeyword>()
        sumResults.forEach { (key, sum) ->
            val difference = average - sum
            resultByKeyword.add(CalculateResultByKeyword(key, sum, difference))
        }

        return CalculateResult(total, wrongList, resultByKeyword)
    }

    /** 集計期間の半年間前から取得する */
    private fun fetchSixMonthsAgoMoneyList(fromDate: LocalDate): List<Money> {
        val sixMonthsAgo = fromDate.minusMonths(6).format(Utils.dateTimeFormatter)
        val condition = mapOf(MoneyConditionType.START_DATE to sixMonthsAgo)
        return fetchMoneyList(condition)
    }

}
