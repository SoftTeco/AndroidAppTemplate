package com.softteco.template.utils

import java.util.regex.Pattern

private val urlPattern: Pattern = Pattern.compile(
    "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" +
        "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+?)*" +
        "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]$~@!:/{};']*)",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
)

fun getHyperLinks(s: String): List<Pair<Int, Int>> {
    val urlList = mutableListOf<Pair<Int, Int>>()
    val urlMatcher = urlPattern.matcher(s)
    while (urlMatcher.find()) {
        urlList.add(Pair(urlMatcher.start(1), urlMatcher.end()))
    }
    return urlList
}
