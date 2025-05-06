package com.vanluong.model

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Created by van.luong
 * on 06,May,2025
 */
data class RecentSearchQuery(
    val query: String,
    val queriedDate: Instant = Clock.System.now(),
)