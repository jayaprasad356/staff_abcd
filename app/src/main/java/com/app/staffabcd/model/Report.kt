package com.app.staffabcd.model

class Report {
    var id: String? = null
    var name: String? = null
    var refer_code: String? = null
    var total_codes: String? = null
    var worked_days: String? = null
    var mobile: String? = null
    var total_referrals: String? = null




    constructor() {}
    constructor(
        id: String?,
        name: String?,
        refer_code: String?,
        total_codes: String?,
        worked_days: String?,
        mobile: String?,
        total_referrals: String?
    ) {
        this.id = id
        this.name = name
        this.refer_code = refer_code
        this.total_codes = total_codes
        this.worked_days = worked_days
        this.mobile = mobile
        this.total_referrals = total_referrals
    }


}