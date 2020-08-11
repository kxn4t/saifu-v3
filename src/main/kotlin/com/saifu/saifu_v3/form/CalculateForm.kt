package com.saifu.saifu_v3.form

import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class CalculateForm(
        @field:NotEmpty(message = "必須です")
        @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日付形式で入力してください（yyyy-MM-dd）")
        val from: String? = null,
        @field:NotEmpty(message = "必須です")
        @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日付形式で入力してください（yyyy-MM-dd）")
        val to: String? = null,
        var keyword: List<String>? = listOf("", "")
) {
    @AssertTrue(message = "必須です")
    fun isKeyword1NotEmpty(): Boolean {
        return keyword?.get(0)?.isNotEmpty() ?: false
    }

    @AssertTrue(message = "必須です")
    fun isKeyword2NotEmpty(): Boolean {
        return keyword?.get(1)?.isNotEmpty() ?: false
    }
}
