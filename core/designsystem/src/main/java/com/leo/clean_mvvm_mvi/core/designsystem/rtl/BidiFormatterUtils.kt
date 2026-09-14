package com.leo.clean_mvvm_mvi.core.designsystem.rtl

import androidx.core.text.BidiFormatter

object BidiFormatterUtils {
    
    fun formatBidi(text: String): String {
        return BidiFormatter.getInstance().unicodeWrap(text)
    }
}
