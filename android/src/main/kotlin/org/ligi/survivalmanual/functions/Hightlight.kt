package org.ligi.survivalmanual.functions


fun highLight(string: String, search: CaseInsensitiveSearch) = string.replace(search.regex) {
    "<font color='red'>${it.value}</font>"
}

fun highLight(string: String, term: String?) = if (term == null) string else highLight(string, CaseInsensitiveSearch(term))