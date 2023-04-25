package com.app.staffabcd.model

class Performance {
    var date: String? = null
    var total_earn: String? = null
    var total_joins: String? = null
    var direct_joins: String? = null

    constructor(
        date: String?,
        total_earn: String?,
        total_joins: String?,
        direct_joins: String?
    ) {
        this.date = date
        this.total_earn = total_earn
        this.total_joins = total_joins
        this.direct_joins = direct_joins
    }
}