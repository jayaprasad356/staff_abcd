package com.app.staffabcd.model

class Incentive {
    var name: String? = null
    var refer_code: String? = null
    var joined_date: String? = null
    var type: String? = null
    var amount: String? = null

    constructor() {}
    constructor(
        name: String?,
        refer_code: String?,
        joined_date: String?,
        type: String?,
        amount:String?

        ) {
        this.name = name
        this.refer_code = refer_code
        this.joined_date = joined_date
        this.type = type
        this.amount=amount

    }
}