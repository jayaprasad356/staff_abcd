package com.app.staffabcd.model

class Incentive {
    var date: String? = null
    var incentives: String? = null
    var total_joinings: String? = null
    var total_leads: String? = null

    constructor() {}
    constructor(
        date: String?,
        incentives: String?,
        total_joinings: String?,
        total_leads: String?,

        ) {
        this.date = date
        this.incentives = incentives
        this.total_joinings = total_joinings
        this.total_leads = total_leads

    }
}