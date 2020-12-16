package com.mobilehealthsports.vaccinepass.util

enum class ThemeColor(val value: Int) {
    PURPLE(1),
    GREEN(2),
    ORANGE(3);

    companion object {
        fun fromInt(value: Int) = ThemeColor.values().firstOrNull { it.value == value }
    }
}