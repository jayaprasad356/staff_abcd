package com.app.staffabcd.model

class Incentives {
    var id: String? = null
    var role: String? = null
    var name: String? = null
    var incentives: String? = null
    var mobile: String? = null
    var branch_name: String? = null


    constructor() {}
    constructor(
        id: String?,
        role: String?,
        name: String?,
        incentives: String?,
        mobile: String?,
        branch_name: String?
    ) {
        this.id = id
        this.role = role
        this.name = name
        this.incentives = incentives
        this.mobile = mobile
        this.branch_name = branch_name
    }



}