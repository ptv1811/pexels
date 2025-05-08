package com.vanluong.common.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

/**
 * Created by van.luong
 * on 09,May,2025
 */
fun String.changeColorAtRange(vararg ranges: Pair<IntRange, Int>): SpannableString {
    val spannableString = SpannableString(this)
    ranges.forEach { (range, color) ->
        if (range.first in 0 until range.last && range.last <= this.length) {
            spannableString.setSpan(
                ForegroundColorSpan(color),
                range.first,
                range.last,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return spannableString
}