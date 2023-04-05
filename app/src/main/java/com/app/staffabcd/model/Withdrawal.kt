package com.app.staffabcd.model

class Withdrawal {
    var status: String? = null
    var amount: String? = null
    var datetime: String? = null

    constructor() {}
    constructor(
        id: String?,
        amount: String?,
        datetime: String?,
    ) {
        this.status = id
        this.amount = amount
        this.datetime = datetime
    }
}