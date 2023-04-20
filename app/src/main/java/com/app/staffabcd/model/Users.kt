package com.app.staffabcd.model

class Users {
    var name: String? = null
    var refer_code: String? = null
    var today_codes: String? = null
    var worked_days: String? = null
    var mobile: String? = null

    constructor() {}
    constructor(
        name: String?,
        refer_code: String?,
        today_codes: String?,
        worked_days: String?,
        mobile: String?
    ) {
        this.name = name
        this.refer_code = refer_code
        this.today_codes = today_codes
        this.worked_days = worked_days
        this.mobile = mobile
    }
}