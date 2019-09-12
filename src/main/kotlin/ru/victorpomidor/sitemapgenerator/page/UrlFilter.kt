package ru.victorpomidor.sitemapgenerator.page

interface UrlFilter {
    fun check(testedUrl: String, baseUrl: String): Boolean
}
