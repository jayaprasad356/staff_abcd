package com.app.staffabcd.model

class Incentives {
    var id:String?=null
    var role:String?=null
    var name: String? = null
    var incentives: String? = null
    var mobile: String? = null



    constructor() {}
    constructor(
        id: String?,
        role: String?,
        first_name: String?,
        incentives: String?,
        mobile: String?
    ) {
        this.id = id
        this.role = role
        this.name = first_name
        this.incentives = incentives
        this.mobile = mobile
    }

}