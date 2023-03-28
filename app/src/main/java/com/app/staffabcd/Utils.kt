package com.app.staffabcd

import android.util.Patterns

class Utils {
    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    fun isValidPhone(mobile: String): Boolean {
        val pattern = Patterns.PHONE
        return pattern.matcher(mobile).matches()
    }
}