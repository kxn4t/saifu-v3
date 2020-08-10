package com.saifu.saifu_v3.model

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
        val id: String,
        val login: String,
        val name: String,
        @JsonProperty("input_count")
        val inputCount: Int,
        @JsonProperty("day_count")
        val dayCount: Int,
        @JsonProperty("repeat_count")
        val repeatCount: Int,
        val day: Int,
        val week: Int,
        val month: Int,
        @JsonProperty("currency_code")
        val currencyCode: String,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String,
        @JsonProperty("cover_image_url")
        val coverImageUrl: String,
        @JsonProperty("profile_modified")
        val profileModified: String
)

data class Verify(
        @JsonProperty("me")
        val user: User,
        val requested: String
)
