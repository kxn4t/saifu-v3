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
import java.time.LocalDateTime

@Service
class MoneyService(
        private val zaimClient: ZaimClient
) {

    fun fetchMoneyList(searchParameters: Map<MoneyConditionType, String> = mapOf()): List<Money> {
        return zaimClient.fetchMoneyList(searchParameters)
    }

    fun updateMoney(money: Money): Boolean {
        return zaimClient.updateMoney(money)
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
        val resultsByKeyword = mutableListOf<CalculateResultByKeyword>()
        sumResults.forEach { (key, sum) ->
            val difference = average - sum
            resultsByKeyword.add(CalculateResultByKeyword(key, sum, difference))
        }

        val forSlack = generateSlackMessage(condition, total, resultsByKeyword)

        return CalculateResult(total, wrongList, resultsByKeyword, forSlack)
    }

    /** 集計期間の半年間前から取得する */
    private fun fetchSixMonthsAgoMoneyList(fromDate: LocalDate): List<Money> {
        val sixMonthsAgo = fromDate.minusMonths(6).format(Utils.dateTimeFormatter)
        val condition = mapOf(MoneyConditionType.START_DATE to sixMonthsAgo)
        return fetchMoneyList(condition)
    }

    // 気合い。。
    private fun generateSlackMessage(
            condition: CalculateForm,
            total: Int,
            resultsByKeyword: List<CalculateResultByKeyword>): String {
        val BREAK = System.getProperty("line.separator")
        val builder = StringBuilder()

        builder.append("<!here> 集計したよ！").append(BREAK)
        // 集計実施日時 e.g. 2020/09/23 12:34:56
        builder.append("＜集計日時＞").append(BREAK)
        builder.append(LocalDateTime.now().format(Utils.zaimDateTimeFormatter)).append(BREAK)
        // 集計期間 e.g. 2020/03/20 00:00:00 〜 2020/09/23 23:59:59
        builder.append("＜集計期間＞").append(BREAK)
        builder.append("${condition.from} 00:00:00 〜 ${condition.to} 23:59:59").append(BREAK)
        // ヘッダ
        builder.append("　　　　　　　　　　　金額  　　　差分金額").append(BREAK)
        // 総合計
        builder.append("総合計 　　　　：${total.displayFormat} 円").append(BREAK)
        // 個々の金額
        resultsByKeyword.forEach { builder.append(generateMessageByKeyword(it)).append(BREAK) }

        print(builder.toString())
        return builder.toString()
    }

    private fun generateMessageByKeyword(data: CalculateResultByKeyword): String {
        val spaceSize = 4 - data.keyword.length.let { if (it <= 4) it else 0 }
        val sb = StringBuilder()
        // 〜の合計　　：
        sb.append("${data.keyword} の合計").apply {
            repeat(spaceSize) { this.append("　") }
        }.append("：")
        //   (合計金額)円
        sb.append("${data.sum.displayFormat} 円　　")
        //   (差分金額)円
        sb.append("${data.difference.displayFormat} 円")
        return sb.toString()
    }

    private val Int.displayFormat get() = String.format("%,8d", this)

}
