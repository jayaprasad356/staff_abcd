package com.app.staffabcd.model

class Report {
    var id: String? = null
    var name: String? = null
    var mobile: String? = null
    var refer_code: String? = null




    constructor() {}
    constructor(
        id: String?,
        name: String?,
        mobile: String?,
        refer_code: String?,
    ) {
        this.id = id
        this.name = name
        this.mobile = mobile
        this.refer_code = refer_code
    }


}