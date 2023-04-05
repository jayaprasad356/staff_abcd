package com.app.staffabcd.model

class Incentive {
    var date: String? = null
    var earning: String? = null
    var joining: String? = null
    var lead: String? = null

    constructor() {}
    constructor(
        date: String?,
        earning: String?,
        joining: String?,
        lead: String?,

    ) {
        this.date = date
        this.earning = earning
        this.joining = joining
        this.lead = lead

    }
}