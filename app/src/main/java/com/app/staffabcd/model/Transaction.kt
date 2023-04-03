package com.app.staffabcd.model

class Transanction {
    var id: String? = null
    var user_id: String? = null
    var codes: String? = null
    var amount: String? = null
    var datetime: String? = null
    var type: String? = null

    constructor() {}
    constructor(
        id: String?,
        user_id: String?,
        codes: String?,
        amount: String?,
        datetime: String?,
        type: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.codes = codes
        this.amount = amount
        this.datetime = datetime
        this.type = type
    }
}