package com.app.staffabcd.model

class Withdrawal {
    var staff_id: String?=null
    var id: String? = null
    var status: String? = null
    var amount: String? = null
    var datetime: String? = null

    constructor() {}
    constructor(
        staff_id:String?,
        id:String?,
        status: String?,
        amount: String?,
        datetime: String?,
    ) {
        this.staff_id=staff_id
        this.id=id
        this.status = status
        this.amount = amount
        this.datetime = datetime
    }
}