package com.app.staffabcd.model

class Report {
    var name: String? = null
    var mobile: String? = null
    var joining: String? = null

    constructor() {}
    constructor(
        name: String?,
        mobile: String?,
        joining: String?,

        ) {
        this.name = name
        this.mobile = mobile
        this.joining = joining


    }
}