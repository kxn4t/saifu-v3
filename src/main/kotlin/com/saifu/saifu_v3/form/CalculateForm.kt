package com.saifu.saifu_v3.form

import com.saifu.saifu_v3.utils.Utils
import com.saifu.saifu_v3.utils.toLocalDate
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class CalculateForm(
        @field:NotEmpty(message = "必須です")
        @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日付形式で入力してください（yyyy-MM-dd）")
        val from: String = "",
        @field:NotEmpty(message = "必須です")
        @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日付形式で入力してください（yyyy-MM-dd）")
        val to: String = "",
        val keyword: List<String> = listOf("", "")
) {
    @AssertTrue(message = "必須です")
    fun isKeyword1NotEmpty(): Boolean {
        return keyword[0].isNotEmpty()
    }

    @AssertTrue(message = "必須です")
    fun isKeyword2NotEmpty(): Boolean {
        return keyword[1].isNotEmpty()
    }

    @AssertTrue(message = "FromはToより以前の日付を設定してください")
    fun isValidDate(): Boolean {
        return try {
            from.toLocalDate(Utils.dateTimeFormatter).isBefore(to.toLocalDate(Utils.dateTimeFormatter))
        } catch (ex: Exception) {
            true
        }
    }
}
