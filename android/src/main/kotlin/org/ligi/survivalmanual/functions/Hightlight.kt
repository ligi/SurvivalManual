package org.ligi.survivalmanual.functions


fun highLight(string: String, word: String?) = string.replace(Regex("(?i)" + word), { "<font color='red'>${it.value}</font>" })