package com.app.staffabcd.model

class Transanction {
    var id: String? = null
    var staff_id: String? = null
    var amount: String? = null
    var date: String? = null
    var type: String? = null

    constructor() {}
    constructor(
        id: String?,
        staff_id: String?,
        amount: String?,
        date: String?,
        type: String?
    ) {
        this.id = id
        this.staff_id = staff_id
        this.amount = amount
        this.date = date
        this.type = type
    }
}