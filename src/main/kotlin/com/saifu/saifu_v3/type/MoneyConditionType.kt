package com.saifu.saifu_v3.type

enum class MoneyConditionType(val key: String) {
    /**
     * narrow down by category_id
     */
    CATEGORY_ID("category_id"),

    /**
     * narrow down by genre_id
     */
    GENRE_ID("genre_id"),

    /**
     * narrow down by type (payment or income or transfer)
     */
    MODE("mode"),

    /**
     * sort by id or date (default : date)
     */
    ORDER("order"),

    /**
     * the first date (YYYY-mm-dd format)
     */
    START_DATE("start_date"),

    /**
     * the last date (YYYY-mm-dd format)
     */
    END_DATE("end_date"),

    /**
     * number of current page (default 1)
     */
    PAGE("page"),

    /**
     * number of items per page (default 20, max 100)
     */
    LIMIT("limit"),

    /**
     * if you set as "receipt_id", Zaim makes the response group by the receipt_id (option)
     */
    GROUP_BY("group_by")

}
