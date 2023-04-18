package com.app.staffabcd.model

class Leaves {
    var id: String? = null
    var date: String? = null
    var type: String? = null
    var user_id: String? = null
    var reason: String? = null
    var status: String? = null

    constructor() {}
    constructor(
        id: String?,
        date: String?,
        type: String?,
        user_id: String?,
        reason: String?,
        status: String?
    ) {
        this.id = id
        this.date = date
        this.type = type
        this.user_id = user_id
        this.reason = reason
        this.status = status
    }
}