package com.app.staffabcd.model

class Incentives {
    var position:String?=null
    var name: String? = null
    var amount: String? = null


    constructor() {}
    constructor(
        position: String?,
        name: String?,
        amount: String?,


        ) {
        this.position = position
        this.name = name
        this.amount = amount


    }
}