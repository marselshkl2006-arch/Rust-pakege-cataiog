package ui.common

/** 1234567 -> "1 234 567" (locale independent). */
fun formatNumber(value: Long): String =
    value.toString().reversed().chunked(3).joinToString(" ").reversed()
