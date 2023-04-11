package com.app.staffabcd.model

class Report {
    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var joining: String? = null
    var level: String? = null
    var history_days: String? = null




    constructor() {}
    constructor(
        id: String?,
        name: String?,
        mobile: String?,
        joining: String?,
        level: String?,
        history_days: String?
    ) {
        this.id = id
        this.name = name
        this.mobile = mobile
        this.joining = joining
        this.level = level
        this.history_days = history_days
    }


}