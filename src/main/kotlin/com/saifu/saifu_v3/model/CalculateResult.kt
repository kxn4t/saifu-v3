package com.saifu.saifu_v3.model

data class CalculateResult(
        /** 総額 */
        val total: Int,
        /** 不一致リスト */
        val wrongList: List<Money>,
        /** キーワード毎集計結果 */
        val resultsByKeyword: List<CalculateResultByKeyword>
)

data class CalculateResultByKeyword(
        /** 集計キーワード */
        val keyword: String,
        /** 小計 */
        val sum: Int,
        /** 金額差分 */
        val difference: Int
)
