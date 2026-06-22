package com.example.pomaryapp.core.utils

object Constants {
    const val DATABASE_NAME = "pomary_db"
    const val TABLE_PREORDER = "preorder_table"
    const val TABLE_PESANAN = "pesanan_table"

    const val DATASTORE_NAME = "pomary_prefs"
    const val KEY_PIN = "user_pin"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_NAME = "user_name"

    const val MAX_PIN_ATTEMPTS = 5
    const val LOCKOUT_DURATION_MS = 30 * 60 * 1000L

    const val COLL_USERS = "users"
    const val COLL_PREORDERS = "preorders"
    const val COLL_ORDERS = "ORDERS"
}