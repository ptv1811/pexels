package com.vanluong.database.util

import androidx.room.TypeConverter
import kotlin.time.Instant

/**
 * Created by van.luong
 * on 06,May,2025
 */
internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}